package in.sportscafe.nostragamus.module.feed.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.login.dto.InfoDetails;

/**
 * Created by deepanshi on 2/1/17.
 */

public class FeedTimeline {

    @SerializedName("matches")
    private List<Match> matches = new ArrayList<>();

    @SerializedName("info")
    private TournamentPowerupInfo tournamentPowerupInfo;

    @SerializedName("powerup_text")
    private String powerupText;

    @SerializedName("matches")
    public List<Match> getMatches() {
        return matches;
    }

    @SerializedName("matches")
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @SerializedName("info")
    public TournamentPowerupInfo getTournamentPowerupInfo() {
        return tournamentPowerupInfo;
    }

    @SerializedName("info")
    public void setTournamentPowerupInfo(TournamentPowerupInfo tournamentPowerupInfo) {
        this.tournamentPowerupInfo = tournamentPowerupInfo;
    }

    @SerializedName("powerup_text")
    public String getPowerupText() {
        return powerupText;
    }

    @SerializedName("powerup_text")
    public void setPowerupText(String powerupText) {
        this.powerupText = powerupText;
    }

}
