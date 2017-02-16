package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;

/**
 * Created by deepanshi on 2/14/17.
 */
public class CompareLeaderBoardResponse {

    @JsonProperty("data")
    private List<CompareLeaderBoard> compareLeaderBoards;

    @JsonProperty("data")
    public List<CompareLeaderBoard> getCompareLeaderBoards() {
        return compareLeaderBoards;
    }

    @JsonProperty("data")
    public void setCompareLeaderBoards(List<CompareLeaderBoard> compareLeaderBoards) {
        this.compareLeaderBoards = compareLeaderBoards;
    }

}
