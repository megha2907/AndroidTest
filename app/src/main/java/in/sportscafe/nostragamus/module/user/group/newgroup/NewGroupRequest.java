package in.sportscafe.nostragamus.module.user.group.newgroup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupRequest {

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("group_img_url")
    private String groupPhoto;

    // Todo remove later
    /*@JsonProperty("group_tournaments")
    private List<Integer> followedTournaments = new ArrayList<Integer>();*/

    @JsonProperty("group_name")
    public String getGroupName() {
        return groupName;
    }

    @JsonProperty("group_name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /*@JsonProperty("group_tournaments")
    public List<Integer> getfollowedTournaments() {
        return followedTournaments;
    }

    @JsonProperty("group_tournaments")
    public void setfollowedTournaments(List<Integer> followedTournaments) {
        this.followedTournaments = followedTournaments;
    }*/

    @JsonProperty("group_img_url")
    public String getGroupPhoto() {
        return groupPhoto;
    }

    @JsonProperty("group_img_url")
    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }
}