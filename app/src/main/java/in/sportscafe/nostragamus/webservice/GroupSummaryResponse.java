package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 10/31/16.
 */
public class GroupSummaryResponse {


    @JsonProperty("data")
    private GroupInfo groupInfo;

    @JsonProperty("data")
    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    @JsonProperty("data")
    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

}
