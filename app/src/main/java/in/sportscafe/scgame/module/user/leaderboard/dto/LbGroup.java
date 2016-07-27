package in.sportscafe.scgame.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class LbGroup {

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("sports")
    private List<LbSport> sports = new ArrayList<>();

    @JsonProperty("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("sports")
    public List<LbSport> getSports() {
        return sports;
    }

    @JsonProperty("sports")
    public void setSports(List<LbSport> sports) {
        this.sports = sports;
    }
}