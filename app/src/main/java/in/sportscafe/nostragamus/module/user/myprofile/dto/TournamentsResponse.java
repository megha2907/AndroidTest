package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 9/28/16.
 */
public class TournamentsResponse {

    @SerializedName("data")
    private List<TournamentInfo> tournamentInfos = new ArrayList<>();

    @SerializedName("data")
    public List<TournamentInfo> getTournamentInfos() {
        return tournamentInfos;
    }

    @SerializedName("data")
    public void setTournamentInfos(List<TournamentInfo> tournamentInfos) {
        this.tournamentInfos = tournamentInfos;
    }
}