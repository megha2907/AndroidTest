package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import in.sportscafe.nostragamus.module.newChallenges.dto.MatchParty;
import in.sportscafe.nostragamus.module.resultspeek.dto.Topics;

/**
 * Created by sandip on 09/09/17.
 */
@Parcel
public class InPlayMatch {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("match_stage")
    private String matchStage;

    @SerializedName("match_venue")
    private String matchVenue;

    @SerializedName("match_type")
    private String matchType;

    @SerializedName("match_parties")
    private List<MatchParty> matchParties = null;

    @SerializedName("match_starttime")
    private String matchStartTime;

    @SerializedName("match_endtime")
    private String matchEndTime;

    @SerializedName("match_result")
    private String matchResult;

    @SerializedName("is_attempted")
    private int isAttempted;

    @SerializedName("match_points")
    private int matchPoints;

    @SerializedName("count_questions")
    private int questionCount;

    @SerializedName("count_answers")
    private int answerCount;

    @SerializedName("match_status")
    private String matchStatus;

    @SerializedName("match_completed")
    private boolean matchCompleted;

    @SerializedName("is_played")
    private boolean isPlayed;

    @SerializedName("match_topic")
    private Topics topics;


    @SerializedName("count_other_contests")
    private int copyAnswerOtherContestCount;

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

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }


    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }


    public int getCopyAnswerPlayedContests() {
        return copyAnswerOtherContestCount;
    }

    public void setCopyAnswerOtherContestCount(int copyAnswerOtherContestCount) {
        this.copyAnswerOtherContestCount = copyAnswerOtherContestCount;
    }
}
