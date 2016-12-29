package in.sportscafe.nostragamus.module.TournamentFeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 11/14/16.
 */

public class TournamentFeedResponse {

    @JsonProperty("data")
    private List<TournamentFeedInfo> tournamentInfos = new ArrayList<>();

    @JsonProperty("data")
    public List<TournamentFeedInfo> getTournamentInfos() {
        return tournamentInfos;
    }
    @JsonProperty("data")
    public void setTournamentInfos(List<TournamentFeedInfo> tournamentInfos) {
        this.tournamentInfos = tournamentInfos;
    }
}
