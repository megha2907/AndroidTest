package in.sportscafe.nostragamus.module.user.group.groupinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;

/**
 * Created by Jeeva on 13/7/16.
 */
public class GroupSportUpdateRequest extends AdminRequest {

    @JsonProperty("sports_preferences")
    private List<Integer> followedSports = new ArrayList<>();

    @JsonProperty("sports_preferences")
    public List<Integer> getFollowedSports() {
        return followedSports;
    }

    @JsonProperty("sports_preferences")
    public void setFollowedSports(List<Integer> followedSports) {
        this.followedSports = followedSports;
    }
}