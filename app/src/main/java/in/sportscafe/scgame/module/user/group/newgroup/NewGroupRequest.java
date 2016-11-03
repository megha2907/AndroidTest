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

    @JsonProperty("group_img_url")
    private String groupPhoto;

    @JsonProperty("group_tournaments")
    private List<Integer> followedTournaments = new ArrayList<Integer>();

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

    @JsonProperty("group_tournaments")
    public List<Integer> getfollowedTournaments() {
        return followedTournaments;
    }

    @JsonProperty("group_tournaments")
    public void setfollowedTournaments(List<Integer> followedTournaments) {
        this.followedTournaments = followedTournaments;
    }

    @JsonProperty("group_img_url")
    public String getGroupPhoto() {
        return groupPhoto;
    }

    @JsonProperty("group_img_url")
    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }
}