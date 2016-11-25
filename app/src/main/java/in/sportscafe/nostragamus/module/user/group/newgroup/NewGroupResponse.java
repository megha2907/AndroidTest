package in.sportscafe.nostragamus.module.user.group.newgroup;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupResponse {

    @JsonProperty("data")
    private GroupInfo groupInfo;

    @JsonProperty("data")
    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    @JsonProperty("data")
    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}