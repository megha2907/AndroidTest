package in.sportscafe.nostragamus.module.user.group.joingroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 11/24/16.
 */

public class JoinGroupResponse {

    @JsonProperty("data")
    private JoinGroup joinGroup;

    @JsonProperty("data")
    public JoinGroup getJoinGroup() {
        return joinGroup;
    }

    @JsonProperty("data")
    public void setJoinGroup(JoinGroup joinGroup) {
        this.joinGroup = joinGroup;
    }
}
