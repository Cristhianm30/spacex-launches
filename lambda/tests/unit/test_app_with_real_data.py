import pytest
from unittest.mock import patch
import app

mock_launch = {
    "id": "6243aec2af52800c6e91925d",
    "name": "USSF-44",
    "date_utc": "2022-11-01T13:41:00.000Z",
    "rocket": "5e9d0d95eda69974db09d1ed",
    "launchpad": "5e9e4502f509094188566f88",
    "payloads": ["5fe3b86eb3467846b324217c"],
    "success": None,
    "links": {
        "webcast": "https://youtu.be/test",
        "article": "https://example.com/article"
    },
    "details": "Test launch details"
}

@patch("app.table")
def test_upsert_launch_with_real_data(mock_table):
    mock_table.put_item.return_value = {}

    result = app.upsert_launch(mock_launch)

    assert result["launch_id"] == "6243aec2af52800c6e91925d"
    assert result["mission_name"] == "USSF-44"
    assert result["rocket_id"] == "5e9d0d95eda69974db09d1ed"
    assert result["launch_date"] == "2022-11-01T13:41:00.000Z"
    assert result["launchpad_id"] == "5e9e4502f509094188566f88"
    assert isinstance(result["payloads"], list)
    assert result["payloads"][0] == "5fe3b86eb3467846b324217c"
    assert result["status"] == "upcoming"
    assert result["links"] == {
        "webcast": "https://youtu.be/test",
        "article": "https://example.com/article"
    }
    assert result["details"] == "Test launch details"