package in.sportscafe.scgame.module.user.group.members;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersRequest {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("group_id")
    public Long getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}