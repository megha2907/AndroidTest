package in.sportscafe.nostragamus.module.user.group.newgroup;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupRequest {

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("group_img_url")
    private String groupPhoto;

    // Todo remove later
    /*@SerializedName("group_tournaments")
    private List<Integer> followedTournaments = new ArrayList<Integer>();*/

    @SerializedName("group_name")
    public String getGroupName() {
        return groupName;
    }

    @SerializedName("group_name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /*@SerializedName("group_tournaments")
    public List<Integer> getfollowedTournaments() {
        return followedTournaments;
    }

    @SerializedName("group_tournaments")
    public void setfollowedTournaments(List<Integer> followedTournaments) {
        this.followedTournaments = followedTournaments;
    }*/

    @SerializedName("group_img_url")
    public String getGroupPhoto() {
        return groupPhoto;
    }

    @SerializedName("group_img_url")
    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }
}