package in.sportscafe.nostragamus.module.user.group.joingroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 11/24/16.
 */

public class JoinGroup {

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
