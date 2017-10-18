package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 10/31/16.
 */
public class GroupSummaryResponse {


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
