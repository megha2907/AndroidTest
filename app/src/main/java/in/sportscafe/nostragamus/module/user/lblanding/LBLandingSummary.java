package in.sportscafe.nostragamus.module.user.lblanding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 19/01/17.
 */

public class LBLandingSummary {

//    @SerializedName("total_points")
//    private Integer totalPoints = 0;

    @SerializedName("title")
    private String leaderBoardTitle;

    @SerializedName("items")
    private List<LbLanding> leaderBoardItems = new ArrayList<>();

//    @SerializedName("total_points")
//    public Integer getTotalPoints() {
//        return totalPoints;}
//
//    @SerializedName("total_points")
//    public void setTotalPoints(Integer totalPoints) {
//        this.totalPoints = totalPoints;
//    }

    @SerializedName("items")
    public List<LbLanding> getLeaderBoardItems() {
        return leaderBoardItems;
    }

    @SerializedName("items")
    public void setLeaderBoardItems(List<LbLanding> leaderBoardItems) {
        this.leaderBoardItems = leaderBoardItems;
    }

    @SerializedName("title")
    public String getLeaderBoardTitle() {
        return leaderBoardTitle;
    }

    @SerializedName("title")
    public void setLeaderBoardTitle(String leaderBoardTitle) {
        this.leaderBoardTitle = leaderBoardTitle;
    }
}