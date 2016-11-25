package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class GroupsDetailResponse {

    @JsonProperty("data")
    private List<GroupInfo> groupsDetail = new ArrayList<>();

    @JsonProperty("data")
    public List<GroupInfo> getGroupsDetail() {
        return groupsDetail;
    }

    @JsonProperty("data")
    public void setGroupsDetail(List<GroupInfo> groupsDetail) {
        this.groupsDetail = groupsDetail;
    }
}