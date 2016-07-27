package in.sportscafe.scgame.module.user.leaderboard;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LeaderBoardResponse {

    @JsonProperty("data")
    private List<LeaderBoard> leaderBoardList = new ArrayList<>();

    @JsonProperty("data")
    public List<LeaderBoard> getLeaderBoardList() {
        return leaderBoardList;
    }

    @JsonProperty("data")
    public void setLeaderBoardList(List<LeaderBoard> leaderBoardList) {
        this.leaderBoardList = leaderBoardList;
    }
}