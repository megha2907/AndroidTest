package in.sportscafe.nostragamus.module.user.group.groupinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;

/**
 * Created by deepanshi on 9/28/16.
 */

public class GroupTournamentUpdateRequest extends AdminRequest {

    @JsonProperty("group_tournaments")
    private List<Integer> followedTournaments = new ArrayList<>();

    @JsonProperty("group_tournaments")
    public List<Integer> getFollowedTournaments() {
        return followedTournaments;
    }

    @JsonProperty("group_tournaments")
    public void setFollowedTournaments(List<Integer> followedTournaments) {
        this.followedTournaments = followedTournaments;
    }
}
