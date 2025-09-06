import json
from unittest.mock import patch, MagicMock
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
            "rocket": "5e9d0d95eda69974db09d1ed",
            "launchpad": "5e9e4502f509094188566f88",
            "payloads": ["5fe3b86eb3467846b324217c"],
            "success": None
        }
    ]
    mock_table.put_item.return_value = {}

    fake_event = {"httpMethod": "GET", "path": "/sync"}
    fake_context = MockContext()

    result = app.lambda_handler(fake_event, fake_context)

    assert result["statusCode"] == 200
    body = json.loads(result["body"])
    assert body["message"] == "SpaceX data synchronization completed successfully"
    assert body["processed_count"] == 1


@patch("app.fetch_spacex_data")
def test_lambda_handler_failure(mock_fetch):
    mock_fetch.side_effect = Exception("API Error")

    fake_event = {"httpMethod": "GET", "path": "/sync"}
    fake_context = MockContext()
    
    result = app.lambda_handler(fake_event, fake_context)

    assert result["statusCode"] == 500
    body = json.loads(result["body"])
    assert "error" in body