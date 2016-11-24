package in.sportscafe.scgame.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;

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
