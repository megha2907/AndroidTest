package in.sportscafe.nostragamus.module.user.group.joingroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 11/24/16.
 */

public class JoinGroup {

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_id")
    public Long getGroupId() {
        return groupId;
    }
    @JsonProperty("group_id")
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
