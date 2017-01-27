package in.sportscafe.nostragamus.module.user.group.members;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersRequest {

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