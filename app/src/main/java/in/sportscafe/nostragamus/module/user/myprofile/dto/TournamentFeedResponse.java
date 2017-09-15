package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 11/14/16.
 */
public class TournamentFeedResponse {

    @SerializedName("data")
    private List<TournamentFeedInfo> tournamentInfos = new ArrayList<>();

    @SerializedName("data")
    public List<TournamentFeedInfo> getTournamentInfos() {
        return tournamentInfos;
    }

    @SerializedName("data")
    public void setTournamentInfos(List<TournamentFeedInfo> tournamentInfos) {
        this.tournamentInfos = tournamentInfos;
    }
}
