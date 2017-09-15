package in.sportscafe.nostragamus.module.user.group.joingroup.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 11/24/16.
 */

public class JoinGroup {

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @SerializedName("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
