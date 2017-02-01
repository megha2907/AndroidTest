package in.sportscafe.nostragamus.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.login.dto.InfoDetails;

/**
 * Created by deepanshi on 2/1/17.
 */

public class FeedTimeline {

    @JsonProperty("matches")
    private List<Match> matches = new ArrayList<>();

    @JsonProperty("matches")
    public List<Match> getMatches() {
        return matches;
    }

    @JsonProperty("matches")
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @JsonProperty("info")
    private TournamentPowerupInfo tournamentPowerupInfo;

    @JsonProperty("info")
    public TournamentPowerupInfo getTournamentPowerupInfo() {
        return tournamentPowerupInfo;
    }

    @JsonProperty("info")
    public void setTournamentPowerupInfo(TournamentPowerupInfo tournamentPowerupInfo) {
        this.tournamentPowerupInfo = tournamentPowerupInfo;
    }

}
