package in.sportscafe.scgame.module.user.group.newgroup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupRequest {

    @JsonProperty("group_created_by")
    private String groupCreatedBy;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("sports_preferences")
    private List<Integer> followedSports = new ArrayList<Integer>();

    @JsonProperty("group_created_by")
    public String getGroupCreatedBy() {
        return groupCreatedBy;
    }

    @JsonProperty("group_created_by")
    public void setGroupCreatedBy(String groupCreatedBy) {
        this.groupCreatedBy = groupCreatedBy;
    }

    @JsonProperty("group_name")
    public String getGroupName() {
        return groupName;
    }

    @JsonProperty("group_name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @JsonProperty("sports_preferences")
    public List<Integer> getFollowedSports() {
        return followedSports;
    }

    @JsonProperty("sports_preferences")
    public void setFollowedSports(List<Integer> followedSports) {
        this.followedSports = followedSports;
    }
}