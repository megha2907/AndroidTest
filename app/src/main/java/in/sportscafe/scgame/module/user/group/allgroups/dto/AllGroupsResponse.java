package in.sportscafe.scgame.module.user.group.allgroups.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import in.sportscafe.scgame.module.user.group.allgroups.AllGroups;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 11/8/16.
 */

public class AllGroupsResponse {

    @JsonProperty("data")
    public List<AllGroups> getAllGroups() {
        return allGroups;
    }

    @JsonProperty("data")
    public void setAllGroups(List<AllGroups> allGroups) {
        this.allGroups = allGroups;
    }

    @JsonProperty("data")
    private List<AllGroups> allGroups;



}
