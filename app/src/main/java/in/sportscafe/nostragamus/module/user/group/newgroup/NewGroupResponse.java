package in.sportscafe.nostragamus.module.user.group.newgroup;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupResponse {

    @SerializedName("data")
    private GroupInfo groupInfo;

    @SerializedName("data")
    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    @SerializedName("data")
    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}