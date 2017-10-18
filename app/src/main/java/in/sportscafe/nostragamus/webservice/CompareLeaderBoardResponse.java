package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;

/**
 * Created by deepanshi on 2/14/17.
 */
public class CompareLeaderBoardResponse {

    @SerializedName("data")
    private List<CompareLeaderBoard> compareLeaderBoards;

    @SerializedName("data")
    public List<CompareLeaderBoard> getCompareLeaderBoards() {
        return compareLeaderBoards;
    }

    @SerializedName("data")
    public void setCompareLeaderBoards(List<CompareLeaderBoard> compareLeaderBoards) {
        this.compareLeaderBoards = compareLeaderBoards;
    }

}
