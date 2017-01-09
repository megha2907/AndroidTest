package in.sportscafe.nostragamus.module.user.group.members;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AddGroupRequest {


    @JsonProperty("group_code")
    private String groupCode;

    @JsonProperty("group_code")
    public String getGroupCode() {
        return groupCode;
    }

    @JsonProperty("group_code")
    public void setGroupCode(String groupId) {
        this.groupCode = groupId;
    }
}