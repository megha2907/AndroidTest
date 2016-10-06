package in.sportscafe.scgame.module.TournamentFeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;

/**
 * Created by deepanshi on 9/28/16.
 */

public class TournamentsResponse {

    @JsonProperty("data")
    private List<TournamentInfo> tournamentInfos = new ArrayList<>();

    @JsonProperty("data")
    public List<TournamentInfo> getTournamentInfos() {
        return tournamentInfos;
    }
    @JsonProperty("data")
    public void setTournamentInfos(List<TournamentInfo> tournamentInfos) {
        this.tournamentInfos = tournamentInfos;
    }

}
