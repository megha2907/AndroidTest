package in.sportscafe.nostragamus.module.challengeCompleted.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedResponse {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("challenge_desc")
    private String challengeDesc;

    @SerializedName("challenge_starttime")
    private String challengeStartTime;

    @SerializedName("challenge_endtime")
    private String challengeEndTime;

    @SerializedName("status")
    private String status = "ongoing";

    @SerializedName("sports_id")
    private int sportsId;

    @SerializedName("is_daily_challenge")
    private boolean isDailyChallenge;

    @SerializedName("contests")
    private List<InPlayContestDto> contestList;

    @SerializedName("challenge_tournaments_short_names")
    private List<String> tournaments = null;


    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getChallengeDesc() {
        return challengeDesc;
    }

    public void setChallengeDesc(String challengeDesc) {
        this.challengeDesc = challengeDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSportsId() {
        return sportsId;
    }

    public void setSportsId(int sportsId) {
        this.sportsId = sportsId;
    }

    public boolean isDailyChallenge() {
        return isDailyChallenge;
    }

    public void setDailyChallenge(boolean dailyChallenge) {
        isDailyChallenge = dailyChallenge;
    }

    public List<InPlayContestDto> getContestList() {
        return contestList;
    }

    public void setContestList(List<InPlayContestDto> contestList) {
        this.contestList = contestList;
    }

    public List<String> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<String> tournaments) {
        this.tournaments = tournaments;
    }

    public String getChallengeStartTime() {
        return challengeStartTime;
    }

    public void setChallengeStartTime(String challengeStartTime) {
        this.challengeStartTime = challengeStartTime;
    }

    public String getChallengeEndTime() {
        return challengeEndTime;
    }

    public void setChallengeEndTime(String challengeEndTime) {
        this.challengeEndTime = challengeEndTime;
    }
}
