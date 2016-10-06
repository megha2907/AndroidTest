package in.sportscafe.scgame.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class MatchesResponse {

    @JsonProperty("data")
    private List<Match> matches = new ArrayList<>();


    @JsonProperty("data")
    public List<Match> getMatches() {
        return matches;
    }

    @JsonProperty("data")
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}