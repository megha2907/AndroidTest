package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 9/15/17.
 */

public class UserLeaderBoardResponse {

    @SerializedName("data")
    private UserLeaderBoardInfo userLeaderBoardInfo;

    public UserLeaderBoardInfo getUserLeaderBoardInfo() {
        return userLeaderBoardInfo;
    }

    public void setUserLeaderBoardInfo(UserLeaderBoardInfo userLeaderBoardInfo) {
        this.userLeaderBoardInfo = userLeaderBoardInfo;
    }
}
