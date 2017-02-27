package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 19/01/17.
 */

public class LBLandingSummary {

//    @JsonProperty("total_points")
//    private Integer totalPoints = 0;

    @JsonProperty("title")
    private String leaderBoardTitle;

    @JsonProperty("items")
    private List<LbLanding> leaderBoardItems = new ArrayList<>();

//    @JsonProperty("total_points")
//    public Integer getTotalPoints() {
//        return totalPoints;}
//
//    @JsonProperty("total_points")
//    public void setTotalPoints(Integer totalPoints) {
//        this.totalPoints = totalPoints;
//    }

    @JsonProperty("items")
    public List<LbLanding> getLeaderBoardItems() {
        return leaderBoardItems;
    }

    @JsonProperty("items")
    public void setLeaderBoardItems(List<LbLanding> leaderBoardItems) {
        this.leaderBoardItems = leaderBoardItems;
    }

    @JsonProperty("title")
    public String getLeaderBoardTitle() {
        return leaderBoardTitle;
    }

    @JsonProperty("title")
    public void setLeaderBoardTitle(String leaderBoardTitle) {
        this.leaderBoardTitle = leaderBoardTitle;
    }
}