package in.sportscafe.nostragamus.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.module.othersanswers.AnswerPercentage;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;

/**
 * Created by Jeeva on 10/6/16.
 */

@Parcel
public class Match {
    @JsonProperty("match_id")
    private int id;

    @JsonProperty("tournament_id")
    private Integer tournamentId;

    @JsonProperty("tournament_name")
    private String tournamentName;

    @JsonProperty("tournament_img_url")
    private String tournamentPhoto;

    @JsonProperty("match_stage")
    private String stage;

    @JsonProperty("match_venue")
    private String venue;

    @JsonProperty("match_parties")
    private List<Parties> parties = new ArrayList<>();

    @JsonProperty("match_starttime")
    private String startTime;

    @JsonProperty("match_endtime")
    private String endTime;

    @JsonProperty("match_result")
    private String result;

    @JsonProperty("match_result_desc")
    private String resultdesc;

    @JsonProperty("match_question_count")
    private Integer matchQuestionCount = 0;

    @JsonProperty("match_points")
    private Integer matchPoints;

    @JsonProperty("correct_count")
    private Integer correctCount = 0;

    @JsonProperty("is_attempted")
    private Integer isAttempted;

    @JsonProperty("match_winner_id")
    private Integer winnerPartyId;

    @JsonProperty("sports_id")
    private Integer sportId;

    @JsonProperty("sports_name")
    private String sportName;

    @JsonProperty("is_result_published")
    private boolean resultPublished;

    @JsonProperty("questions")
    private List<Question> questions = new ArrayList<>();

    @JsonProperty("challenge_id")
    private Integer challengeId;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("challenge_img_url")
    private String challengeImgUrl;

    @JsonProperty("avg_match_points")
    private Double avgMatchPoints;

    @JsonProperty("highest_match_points")
    private Integer highestMatchPoints;

    @JsonProperty("rank")
    private Integer userRank;

    @JsonProperty("rank_change")
    private Integer rankChange;

    @JsonProperty("count_powerups")
    private Integer countPowerUps;

    @JsonProperty("count_match_powerups")
    private Integer countMatchPowerupsUsed;

    @JsonProperty("count_challenge_players")
    private Integer countPlayers;

    @JsonProperty("count_match_players")
    private Integer countMatchPlayers;

    @JsonProperty("highest_player_id")
    private Integer highestScorerId;

    @JsonProperty("highest_player_name")
    private String highestScorerName;

    @JsonProperty("highest_player_photo")
    private String highestScorerPhoto;


    /**
     *
     * @return
     * The id
     */
    @JsonProperty("match_id")
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The match_id
     */
    @JsonProperty("match_id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The tournamentId
     */
    @JsonProperty("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    /**
     *
     * @param tournamentId
     * The tournament_id
     */
    @JsonProperty("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @JsonProperty("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    @JsonProperty("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     *
     * @return
     * The stage
     */
    @JsonProperty("match_stage")
    public String getStage() {
        return stage;
    }

    /**
     *
     * @param stage
     * The match_stage
     */
    @JsonProperty("match_stage")
    public void setStage(String stage) {
        this.stage = stage;
    }

    /**
     *
     * @return
     * The venue
     */
    @JsonProperty("match_venue")
    public String getVenue() {
        return venue;
    }

    /**
     *
     * @param venue
     * The match_venue
     */
    @JsonProperty("match_venue")
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @JsonProperty("match_parties")
    public List<Parties> getParties() {
        return parties;
    }

    @JsonProperty("match_parties")
    public void setParties(List<Parties> parties) {
        this.parties = parties;
    }

    /**
     *
     * @return
     * The startTime
     */
    @JsonProperty("match_starttime")
    public String getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime
     * The match_starttime
     */
    @JsonProperty("match_starttime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return
     * The endTime
     */
    @JsonProperty("match_endtime")
    public String getEndTime() {
        return endTime;
    }

    /**
     *
     * @param endTime
     * The match_endtime
     */
    @JsonProperty("match_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return
     * The result
     */
    @JsonProperty("match_result")
    public String getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The match_result
     */
    @JsonProperty("match_result")
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     * @return
     * The questionsLive
     */
    @JsonProperty("is_attempted")
    public Integer getisAttempted() {
        return isAttempted;
    }

    /**
     *
     * @param isAttempted
     * The match_questions_live
     */
    @JsonProperty("is_attempted")
    public void setisAttemoted(Integer isAttempted) {
        this.isAttempted = isAttempted;
    }

    @JsonProperty("questions")
    public List<Question> getQuestions() {
        return questions;
    }

    @JsonProperty("questions")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @JsonProperty("match_question_count")
    public Integer getMatchQuestionCount() {
        return matchQuestionCount;
    }

    @JsonProperty("match_question_count")
    public void setMatchQuestionCount(int matchQuestionCount) {
        this.matchQuestionCount = matchQuestionCount;
    }

    @JsonProperty("match_points")
    public Integer getMatchPoints() {
        return matchPoints;
    }

    @JsonProperty("match_points")
    public void setMatchPoints(Integer matchPoints) {
        this.matchPoints = matchPoints;
    }

    @JsonProperty("correct_count")
    public Integer getCorrectCount() {
        if(null == correctCount) {
            return 0;
        }
        return correctCount;
    }

    @JsonProperty("correct_count")
    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    @JsonProperty("tournament_img_url")
    public String getTournamentPhoto() {
        return tournamentPhoto;
    }

    @JsonProperty("tournament_img_url")
    public void setTournamentPhoto(String tournamentPhoto) {
        this.tournamentPhoto = tournamentPhoto;
    }

    @JsonProperty("match_winner_id")
    public Integer getWinnerPartyId() {
        return winnerPartyId;
    }

    @JsonProperty("match_winner_id")
    public void setWinnerPartyId(Integer winnerPartyId) {
        this.winnerPartyId = winnerPartyId;
    }

    @JsonProperty("match_result_desc")
    public String getResultdesc() {
        return resultdesc;
    }

    @JsonProperty("match_result_desc")
    public void setResultdesc(String resultdesc) {
        this.resultdesc = resultdesc;
    }

    @JsonIgnore
    public void updateAnswerPercentage(Map<Integer, AnswerPercentage> questionAnswersMap) {
        for (Question question : questions) {
            question.updatePollPercentage(questionAnswersMap.get(question.getQuestionId()));
        }
    }

    @JsonProperty("sports_id")
    public Integer getSportId() {
        return sportId;
    }

    @JsonProperty("sports_id")
    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    @JsonProperty("sports_name")
    public String getSportName() {
        return sportName;
    }

    @JsonProperty("sports_name")
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    @JsonProperty("is_result_published")
    public boolean isResultPublished() {
        return resultPublished;
    }

    @JsonProperty("is_result_published")
    public void setResultPublished(boolean resultPublished) {
        this.resultPublished = resultPublished;
    }

    @JsonProperty("challenge_id")
    public Integer getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("challenge_name")
    public String getChallengeName() {
        return challengeName;
    }

    @JsonProperty("challenge_name")
    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    @JsonProperty("challenge_img_url")
    public String getChallengeImgUrl() {
        return challengeImgUrl;
    }

    @JsonProperty("challenge_img_url")
    public void setChallengeImgUrl(String challengeImgUrl) {
        this.challengeImgUrl = challengeImgUrl;
    }

    @JsonProperty("avg_match_points")
    public Double getAvgMatchPoints() {
        return avgMatchPoints;
    }

    @JsonProperty("avg_match_points")
    public void setAvgMatchPoints(Double avgMatchPoints) {
        this.avgMatchPoints = avgMatchPoints;
    }

    @JsonProperty("highest_match_points")
    public Integer getHighestMatchPoints() {
        return highestMatchPoints;
    }

    @JsonProperty("highest_match_points")
    public void setHighestMatchPoints(Integer highestMatchPoints) {
        this.highestMatchPoints = highestMatchPoints;
    }

    @JsonProperty("rank")
    public Integer getUserRank() {
        return userRank;
    }

    @JsonProperty("rank")
    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    @JsonProperty("count_powerups")
    public Integer getCountPowerUps() {
        return countPowerUps;
    }

    @JsonProperty("count_powerups")
    public void setCountPowerUps(Integer countPowerUps) {
        this.countPowerUps = countPowerUps;
    }

    @JsonProperty("count_challenge_players")
    public Integer getCountPlayers() {
        return countPlayers;
    }

    @JsonProperty("count_challenge_players")
    public void setCountPlayers(Integer countPlayers) {
        this.countPlayers = countPlayers;
    }

    @JsonProperty("count_match_powerups")
    public Integer getCountMatchPowerupsUsed() {
        return countMatchPowerupsUsed;
    }

    @JsonProperty("count_match_powerups")
    public void setCountMatchPowerupsUsed(Integer countMatchPowerupsUsed) {
        this.countMatchPowerupsUsed = countMatchPowerupsUsed;
    }

    @JsonProperty("count_match_players")
    public Integer getCountMatchPlayers() {
        return countMatchPlayers;
    }

    @JsonProperty("count_match_players")
    public void setCountMatchPlayers(Integer countMatchPlayers) {
        this.countMatchPlayers = countMatchPlayers;
    }

    @JsonProperty("highest_player_id")
    public Integer getHighestScorerId() {
        return highestScorerId;
    }

    @JsonProperty("highest_player_id")
    public void setHighestScorerId(Integer highestScorerId) {
        this.highestScorerId = highestScorerId;
    }

    @JsonProperty("highest_player_name")
    public String getHighestScorerName() {
        return highestScorerName;
    }

    @JsonProperty("highest_player_name")
    public void setHighestScorerName(String highestScorerName) {
        this.highestScorerName = highestScorerName;
    }

    @JsonProperty("highest_player_photo")
    public String getHighestScorerPhoto() {
        return highestScorerPhoto;
    }

    @JsonProperty("highest_player_photo")
    public void setHighestScorerPhoto(String highestScorerPhoto) {
        this.highestScorerPhoto = highestScorerPhoto;
    }

    @JsonProperty("rank_change")
    public Integer getRankChange() {
        return rankChange;
    }

    @JsonProperty("rank_change")
    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }


}