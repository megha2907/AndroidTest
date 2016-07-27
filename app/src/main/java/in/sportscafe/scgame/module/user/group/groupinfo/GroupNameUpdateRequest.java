package in.sportscafe.scgame.module.user.group.groupinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.scgame.module.user.group.members.AdminRequest;

/**
 * Created by Jeeva on 13/7/16.
 */
public class GroupNameUpdateRequest extends AdminRequest {

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("group_name")
    public String getGroupName() {
        return groupName;
    }

    @JsonProperty("group_name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}