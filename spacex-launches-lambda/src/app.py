import os
import json
import logging
import requests
import boto3
from botocore.exceptions import ClientError
from typing import Dict, List, Any, Optional

SPACEX_API_URL = os.environ.get("SPACEX_API_URL", "https://api.spacexdata.com/v4/launches")
SPACEX_AWS_REGION = os.environ.get("SPACEX_AWS_REGION", "us-east-1")
SPACEX_TABLE_NAME = os.environ.get("SPACEX_TABLE_NAME", "spacex-launches")
SPACEX_REQUEST_TIMEOUT = int(os.environ.get("SPACEX_REQUEST_TIMEOUT", "30"))
LOG_LEVEL = os.environ.get("LOG_LEVEL", "INFO")

logger = logging.getLogger()
logger.setLevel(getattr(logging, LOG_LEVEL.upper()))

dynamodb = boto3.resource("dynamodb", region_name=SPACEX_AWS_REGION)
table = dynamodb.Table(SPACEX_TABLE_NAME)

LAUNCH_STATUS = {
    "UPCOMING": "upcoming",
    "SUCCESS": "success",
    "FAILED": "failed"
}


def fetch_spacex_data() -> List[Dict[str, Any]]:

    try:
        logger.debug(f"Fetching SpaceX data from: {SPACEX_API_URL}")
        response = requests.get(SPACEX_API_URL, timeout=SPACEX_REQUEST_TIMEOUT)
        response.raise_for_status()
        
        data = response.json()
        logger.debug(f"Fetched {len(data)} launches from SpaceX API")
        return data
        
    except requests.RequestException as e:
        logger.error(f"Failed to fetch SpaceX data: {str(e)}")
        raise
    except json.JSONDecodeError as e:
        logger.error(f"Failed to parse SpaceX API response: {str(e)}")
        raise


def determine_launch_status(launch: Dict[str, Any]) -> str:
    success = launch.get("success")
    
    if success is True:
        return LAUNCH_STATUS["SUCCESS"]
    elif success is False:
        return LAUNCH_STATUS["FAILED"]
    else:
        return LAUNCH_STATUS["UPCOMING"]


def upsert_launch(launch: Dict[str, Any]) -> Dict[str, Any]:
    try:
        launch_id = launch.get("id")
        if not launch_id:
            raise ValueError("Launch ID is missing from launch data")
            
        status = determine_launch_status(launch)
        
        item = {
            "launch_id": launch_id,
            "mission_name": launch.get("name", "Unknown Mission"),
            "rocket_id": launch.get("rocket", ""),
            "launch_date": launch.get("date_utc", ""),
            "launchpad_id": launch.get("launchpad", ""),
            "payloads": launch.get("payloads", []),
            "status": status,
            "links": launch.get("links", {}),
            "details": launch.get("details", "")
        }
        
        logger.debug(f"Upserting launch: {launch_id} - {item.get('mission_name')}")
        table.put_item(Item=item)
        
        return item
        
    except ClientError as e:
        error_code = e.response.get('Error', {}).get('Code', 'Unknown')
        logger.error(f"DynamoDB error ({error_code}) while upserting launch {launch.get('id', 'unknown')}: {str(e)}")
        raise
    except Exception as e:
        logger.error(f"Unexpected error while processing launch {launch.get('id', 'unknown')}: {str(e)}")
        raise


def process_launches(launches: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
    successful_count = 0
    failed_count = 0
    
    with table.batch_writer() as batch:
        for launch in launches:
            try:
                status = determine_launch_status(launch)
                
                item = {
                    "launch_id": launch.get("id"),
                    "mission_name": launch.get("name", "Unknown Mission"),
                    "rocket_id": launch.get("rocket", ""),
                    "launch_date": launch.get("date_utc", ""),
                    "launchpad_id": launch.get("launchpad", ""),
                    "payloads": launch.get("payloads", []),
                    "status": status,
                    "links": launch.get("links", {}),
                    "details": launch.get("details", "")
                }

                if not item["launch_id"]:
                    logger.warning(f"Skipping launch with missing ID: {launch.get('name')}")
                    failed_count += 1
                    continue

                batch.put_item(Item=item)
                successful_count += 1
            
            except Exception as e:
                failed_count += 1
                logger.error(f"Failed to process launch {launch.get('id', 'unknown')} for batching: {str(e)}")
                continue

    logger.info(f"Processing completed: {successful_count} successful, {failed_count} failed")
    return {"processed": successful_count, "failed": failed_count}


def lambda_handler(event: Dict[str, Any], context: Any) -> Dict[str, Any]:

    request_id = getattr(context, 'aws_request_id', 'unknown')
    logger.info(f"Lambda execution started - Request ID: {request_id}")
    
    try:
        launches = fetch_spacex_data()
        
        if not launches:
            logger.warning("No launch data received from SpaceX API")
            return {
                "statusCode": 200,
                "body": json.dumps({
                    "message": "No launch data to synchronize",
                    "count": 0,
                    "request_id": request_id
                })
            }
        
        results_summary = process_launches(launches)
        
        response_body = {
            "message": "SpaceX data synchronization completed successfully",
            "total_launches_fetched": len(launches),
            "successful_items": results_summary.get("processed", 0),
            "failed_items": results_summary.get("failed", 0),       
            "request_id": request_id
        }
        
        logger.info(f"Lambda execution completed successfully: {response_body}")
        
        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps(response_body)
        }
        
    except requests.RequestException as e:
        error_message = f"Failed to fetch data from SpaceX API: {str(e)}"
        logger.error(f"API error - {error_message}")
        
        return {
            "statusCode": 502,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "error": "External API error",
                "message": error_message,
                "request_id": request_id
            })
        }
        
    except Exception as e:
        error_message = f"Unexpected error during Lambda execution: {str(e)}"
        logger.critical(f"Lambda error - {error_message}")
        
        return {
            "statusCode": 500,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "error": "Internal server error",
                "message": str(e),
                "request_id": request_id
            })
        }