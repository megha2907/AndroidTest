package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class LbGroupsList {

    @JsonProperty("groups")
    private List<LbGroup> groups = new ArrayList<>();

    @JsonProperty("data")
    public List<LbGroup> getGroups() {
        return groups;
    }

    @JsonProperty("data")
    public void setGroups(List<LbGroup> groups) {
        this.groups = groups;
    }
}