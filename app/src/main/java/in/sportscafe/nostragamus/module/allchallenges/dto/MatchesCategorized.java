package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by sandip on 01/05/17.
 */

@Parcel
public class MatchesCategorized {

    @JsonProperty("All")
    private List<Match> allMatches = new ArrayList<>();

    @JsonProperty("To Play")
    private List<Match> toPlayMatches = new ArrayList<>();

    @JsonProperty("All")
    public List<Match> getAllMatches() {
        return allMatches;
    }

    @JsonProperty("All")
    public void setAllMatches(List<Match> matches) {
        this.allMatches = matches;
    }

    @JsonProperty("To Play")
    public List<Match> getToPlayMatches() {
        return toPlayMatches;
    }

    @JsonProperty("To Play")
    public void setToPlayMatches(List<Match> toPlayMatches) {
        this.toPlayMatches = toPlayMatches;
    }
}
