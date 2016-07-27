package in.sportscafe.scgame.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 30/6/16.
 */
public class GroupsLbResponse {

    @JsonProperty("data")
    private LbGroupsList lbGroupsList;

    @JsonProperty("data")
    public LbGroupsList getLbGroupsList() {
        return lbGroupsList;
    }

    @JsonProperty("data")
    public void setLbGroupsList(LbGroupsList lbGroupsList) {
        this.lbGroupsList = lbGroupsList;
    }
}