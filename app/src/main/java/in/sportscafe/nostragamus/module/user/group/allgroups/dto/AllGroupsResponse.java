package in.sportscafe.nostragamus.module.user.group.allgroups.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroups;

/**
 * Created by deepanshi on 11/8/16.
 */

public class AllGroupsResponse {

    @SerializedName("data")
    public List<AllGroups> getAllGroups() {
        return allGroups;
    }

    @SerializedName("data")
    public void setAllGroups(List<AllGroups> allGroups) {
        this.allGroups = allGroups;
    }

    @SerializedName("data")
    private List<AllGroups> allGroups;


}
