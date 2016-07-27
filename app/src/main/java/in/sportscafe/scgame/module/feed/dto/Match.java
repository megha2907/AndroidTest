package in.sportscafe.scgame.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.module.play.prediction.dto.Question;

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

    @JsonProperty("match_stage")
    private String stage;

    @JsonProperty("match_venue")
    private String venue;

    @JsonProperty("match_parties")
    private String parties;

    @JsonProperty("match_starttime")
    private String startTime;

    @JsonProperty("match_endtime")
    private String endTime;

    @JsonProperty("match_result")
    private String result;

    @JsonProperty("match_questions_live")
    private boolean questionsLive;

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
    @JsonProperty("match_parties")
    public String getParties() {
        return parties;
    }

    /**
     *
     * @param parties
     * The match_parties
     */
    @JsonProperty("match_parties")
    public void setParties(String parties) {
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
    @JsonProperty("match_questions_live")
    public boolean getQuestionsLive() {
        return questionsLive;
    }

    /**
     *
     * @param questionsLive
     * The match_questions_live
     */
    @JsonProperty("match_questions_live")
    public void setQuestionsLive(boolean questionsLive) {
        this.questionsLive = questionsLive;
    }

    @JsonProperty("questions")
    public List<Question> getQuestions() {
        return questions;
    }

    @JsonProperty("questions")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}