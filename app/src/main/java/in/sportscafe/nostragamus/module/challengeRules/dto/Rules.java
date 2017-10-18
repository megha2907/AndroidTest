package in.sportscafe.nostragamus.module.challengeRules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.dto.ContestModeInfo;
import in.sportscafe.nostragamus.module.contest.dto.PowerUpInfo;

/**
 * Created by deepanshi on 9/15/17.
 */

@Parcel
public class Rules {

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("contest_name")
    private String contestName;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("tot_matches")
    private int totalMatches;

    @SerializedName("max_transfer_limit")
    private int maxTransferPowerUps;

    @SerializedName("challenge_tournaments_short_names")
    private List<String> tournaments = new ArrayList<>();

    @SerializedName("powerups")
    private PowerUpInfo powerUpInfo;

    @SerializedName("mode")
    private ContestModeInfo contestModeInfo;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public List<String> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<String> tournaments) {
        this.tournaments = tournaments;
    }

    public PowerUpInfo getPowerUpInfo() {
        return powerUpInfo;
    }

    public void setPowerUpInfo(PowerUpInfo powerUpInfo) {
        this.powerUpInfo = powerUpInfo;
    }

    public ContestModeInfo getContestModeInfo() {
        return contestModeInfo;
    }

    public void setContestModeInfo(ContestModeInfo contestModeInfo) {
        this.contestModeInfo = contestModeInfo;
    }

    public int getMaxTransferPowerUps() {
        return maxTransferPowerUps;
    }

    public void setMaxTransferPowerUps(int maxTransferPowerUps) {
        this.maxTransferPowerUps = maxTransferPowerUps;
    }

}
