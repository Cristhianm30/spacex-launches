import pytest
from unittest.mock import patch, MagicMock
import sys
import os
sys.path.append(os.path.join(os.path.dirname(__file__), '..', '..', 'src'))
import app

mock_launch_upcoming = {
    "id": "6243aec2af52800c6e91925d",
    "name": "USSF-44",
    "date_utc": "2022-11-01T13:41:00.000Z",
    "rocket": "5e9d0d95eda69974db09d1ed",
    "launchpad": "5e9e4502f509094188566f88",
    "payloads": ["5fe3b86eb3467846b324217c"],
    "success": None,
    "links": {"webcast": "https://youtu.be/test"},
    "details": "Test launch details"
}

@patch("app.table")
def test_process_launches_formats_and_batches_item(mock_table):
    mock_batch = MagicMock()
    mock_table.batch_writer.return_value.__enter__.return_value = mock_batch


    launches_list = [mock_launch_upcoming]
    result_summary = app.process_launches(launches_list)

    assert result_summary["processed"] == 1
    assert result_summary["failed"] == 0

    expected_item = {
        "launch_id": "6243aec2af52800c6e91925d",
        "mission_name": "USSF-44",
        "rocket_id": "5e9d0d95eda69974db09d1ed",
        "launch_date": "2022-11-01T13:41:00.000Z",
        "launchpad_id": "5e9e4502f509094188566f88",
        "payloads": ["5fe3b86eb3467846b324217c"],
        "status": "upcoming", 
        "links": {"webcast": "https://youtu.be/test"},
        "details": "Test launch details"
    }

    mock_batch.put_item.assert_called_once_with(Item=expected_item)