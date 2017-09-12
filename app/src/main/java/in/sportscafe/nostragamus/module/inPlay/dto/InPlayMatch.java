package in.sportscafe.nostragamus.module.inPlay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatch {

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("match_id")
    private int matchId;

    @JsonProperty("match_stage")
    private String matchStage;

    @JsonProperty("match_venue")
    private String matchVenue;

    @JsonProperty("match_type")
    private String matchType;

    @JsonProperty("match_parties")
    private List<MatchParty> matchParties = null;

    @JsonProperty("match_starttime")
    private String matchStartTime;

    @JsonProperty("match_endtime")
    private String matchEndTime;

    @JsonProperty("match_result")
    private String matchResult;

    @JsonProperty("is_attempted")
    private int isAttempted;

    @JsonProperty("match_points")
    private int matchPoints;

    @JsonProperty("q_count")
    private int questionCount;

    @JsonProperty("match_status")
    private String matchStatus;

    @JsonProperty("match_completed")
    private boolean matchCompleted;

    @JsonProperty("is_played")
    private boolean isPlayed;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getMatchStage() {
        return matchStage;
    }

    public void setMatchStage(String matchStage) {
        this.matchStage = matchStage;
    }

    public String getMatchVenue() {
        return matchVenue;
    }

    public void setMatchVenue(String matchVenue) {
        this.matchVenue = matchVenue;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public List<MatchParty> getMatchParties() {
        return matchParties;
    }

    public void setMatchParties(List<MatchParty> matchParties) {
        this.matchParties = matchParties;
    }

    public String getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public String getMatchEndTime() {
        return matchEndTime;
    }

    public void setMatchEndTime(String matchEndTime) {
        this.matchEndTime = matchEndTime;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public int getIsAttempted() {
        return isAttempted;
    }

    public void setIsAttempted(int isAttempted) {
        this.isAttempted = isAttempted;
    }

    public int getMatchPoints() {
        return matchPoints;
    }

    public void setMatchPoints(int matchPoints) {
        this.matchPoints = matchPoints;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public boolean isMatchCompleted() {
        return matchCompleted;
    }

    public void setMatchCompleted(boolean matchCompleted) {
        this.matchCompleted = matchCompleted;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

}
