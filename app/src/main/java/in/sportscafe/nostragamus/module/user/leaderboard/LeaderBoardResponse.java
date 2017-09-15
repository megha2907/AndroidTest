package in.sportscafe.nostragamus.module.user.leaderboard;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LeaderBoardResponse {

    @SerializedName("data")
    private List<LeaderBoard> leaderBoardList = new ArrayList<>();

    @SerializedName("data")
    public List<LeaderBoard> getLeaderBoardList() {
        return leaderBoardList;
    }

    @SerializedName("data")
    public void setLeaderBoardList(List<LeaderBoard> leaderBoardList) {
        this.leaderBoardList = leaderBoardList;
    }
}