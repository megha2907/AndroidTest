package in.sportscafe.nostragamus.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.module.othersanswers.AnswerPercentage;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;

/**
 * Created by Jeeva on 10/6/16.
 */
public class Match implements Serializable {
    @JsonProperty("match_id")
    private Integer id;

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
    private Integer matchQuestionCount;

    @JsonProperty("match_points")
    private Integer matchPoints;

    @JsonProperty("correct_count")
    private Integer correctCount;

    @JsonProperty("is_attempted")
    private boolean isAttempted;

    @JsonProperty("match_winner_id")
    private Integer winnerPartyId;

    @JsonProperty("sports_id")
    private Integer sportId;

    @JsonProperty("sports_name")
    private String sportName;

//    @JsonProperty("match_commentary")
//    private String matchCommentary;

    @JsonProperty("questions")
    private List<Question> questions = new ArrayList<>();

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("match_id")
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The match_id
     */
    @JsonProperty("match_id")
    public void setId(Integer id) {
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

    /**
     *
     * @return
     * The parties
     */
//    @JsonProperty("match_parties")
//    public String getParties() {
//        return parties;
//    }


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
     * @param parties
     * The match_parties
     */

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
    public boolean getisAttempted() {
        return isAttempted;
    }

    /**
     *
     * @param isAttempted
     * The match_questions_live
     */
    @JsonProperty("is_attempted")
    public void setisAttemoted(boolean isAttempted) {
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
    public void setMatchPoints(int matchPoints) {
        this.matchPoints = matchPoints;
    }

    @JsonProperty("correct_count")
    public Integer getCorrectCount() {
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

//
//    @JsonProperty("match_commentary")
//    public String getMatchCommentary() {
//        return matchCommentary;
//    }
//
//    @JsonProperty("match_commentary")
//    public void setMatchCommentary(String matchCommentary) {
//        this.matchCommentary = matchCommentary;
//    }

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
}