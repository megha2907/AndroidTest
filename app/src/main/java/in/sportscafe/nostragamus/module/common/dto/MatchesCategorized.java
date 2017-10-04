package in.sportscafe.nostragamus.module.common.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.resultspeek.dto.Match;


/**
 * Created by sandip on 01/05/17.
 */

@Parcel
public class MatchesCategorized {

    @SerializedName("All")
    private List<Match> allMatches = new ArrayList<>();

    @SerializedName("To Play")
    private List<Match> toPlayMatches = new ArrayList<>();

    @SerializedName("All")
    public List<Match> getAllMatches() {
        return allMatches;
    }

    @SerializedName("All")
    public void setAllMatches(List<Match> matches) {
        this.allMatches = matches;
    }

    @SerializedName("To Play")
    public List<Match> getToPlayMatches() {
        return toPlayMatches;
    }

    @SerializedName("To Play")
    public void setToPlayMatches(List<Match> toPlayMatches) {
        this.toPlayMatches = toPlayMatches;
    }
}
