package in.sportscafe.nostragamus.module.user.group.joingroup.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 11/24/16.
 */

public class JoinGroupResponse {

    @SerializedName("data")
    private JoinGroup joinGroup;

    @SerializedName("data")
    public JoinGroup getJoinGroup() {
        return joinGroup;
    }

    @SerializedName("data")
    public void setJoinGroup(JoinGroup joinGroup) {
        this.joinGroup = joinGroup;
    }
}
