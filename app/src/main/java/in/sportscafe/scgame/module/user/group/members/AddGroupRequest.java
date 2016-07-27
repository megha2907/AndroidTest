package in.sportscafe.scgame.module.user.group.members;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AddGroupRequest {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("group_code")
    private String groupCode;

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("group_code")
    public String getGroupCode() {
        return groupCode;
    }

    @JsonProperty("group_code")
    public void setGroupCode(String groupId) {
        this.groupCode = groupId;
    }
}