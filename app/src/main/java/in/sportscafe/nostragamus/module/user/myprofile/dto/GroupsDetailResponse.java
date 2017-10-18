package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class GroupsDetailResponse {

    @SerializedName("data")
    private List<GroupInfo> groupsDetail = new ArrayList<>();

    @SerializedName("data")
    public List<GroupInfo> getGroupsDetail() {
        return groupsDetail;
    }

    @SerializedName("data")
    public void setGroupsDetail(List<GroupInfo> groupsDetail) {
        this.groupsDetail = groupsDetail;
    }
}