package in.sportscafe.nostragamus.module.user.group.members;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AddGroupRequest {


    @SerializedName("group_code")
    private String groupCode;

    @SerializedName("group_code")
    public String getGroupCode() {
        return groupCode;
    }

    @SerializedName("group_code")
    public void setGroupCode(String groupId) {
        this.groupCode = groupId;
    }
}