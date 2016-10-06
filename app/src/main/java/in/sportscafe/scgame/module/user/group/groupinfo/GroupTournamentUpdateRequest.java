package in.sportscafe.scgame.module.user.group.groupinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.group.members.AdminRequest;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

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
