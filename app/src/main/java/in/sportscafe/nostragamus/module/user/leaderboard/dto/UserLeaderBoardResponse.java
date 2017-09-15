package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 9/15/17.
 */

public class UserLeaderBoardResponse {

    @JsonProperty("data")
    private UserLeaderBoardInfo userLeaderBoardInfo;

    public UserLeaderBoardInfo getUserLeaderBoardInfo() {
        return userLeaderBoardInfo;
    }

    public void setUserLeaderBoardInfo(UserLeaderBoardInfo userLeaderBoardInfo) {
        this.userLeaderBoardInfo = userLeaderBoardInfo;
    }
}
