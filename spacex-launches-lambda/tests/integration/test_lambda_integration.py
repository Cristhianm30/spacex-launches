import json
from unittest.mock import patch, MagicMock
import sys
import os
sys.path.append(os.path.join(os.path.dirname(__file__), '..', '..', 'src'))
import app

class MockContext:
    def __init__(self):
        self.aws_request_id = "test-request-123"

@patch("app.table")
@patch("app.fetch_spacex_data")
def test_lambda_handler_success(mock_fetch, mock_table):
    mock_fetch.return_value = [
        {
            "id": "6243aec2af52800c6e91925d",
            "name": "USSF-44",
            "date_utc": "2022-11-01T13:41:00.000Z",
            "success": None,
        }
    ]

    mock_batch = MagicMock()
    mock_table.batch_writer.return_value.__enter__.return_value = mock_batch

    fake_event = {"httpMethod": "GET", "path": "/sync"}
    fake_context = MockContext()

    result = app.lambda_handler(fake_event, fake_context)

    assert result["statusCode"] == 200
    body = json.loads(result["body"])
    assert body["message"] == "SpaceX data synchronization completed successfully"
    assert body["total_launches_fetched"] == 1
    assert body["successful_items"] == 1
    assert body["failed_items"] == 0

@patch("app.fetch_spacex_data")
def test_lambda_handler_failure(mock_fetch):
    mock_fetch.side_effect = Exception("API Error")

    fake_event = {"httpMethod": "GET", "path": "/sync"}
    fake_context = MockContext()
    
    result = app.lambda_handler(fake_event, fake_context)

    assert result["statusCode"] == 500
    body = json.loads(result["body"])
    assert "error" in body