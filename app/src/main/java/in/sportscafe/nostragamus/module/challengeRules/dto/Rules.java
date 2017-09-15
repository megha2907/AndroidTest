package in.sportscafe.nostragamus.module.challengeRules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("contest_id")
    private int contestId;

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("contest_name")
    private String contestName;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("tot_matches")
    private int totalMatches;

    @JsonProperty("max_transfer_limit")
    private int maxTransferPowerUps;

    @JsonProperty("challenge_tournaments_short_names")
    private List<String> tournaments = new ArrayList<>();

    @JsonProperty("powerups")
    private PowerUpInfo powerUpInfo;

    @JsonProperty("mode")
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
