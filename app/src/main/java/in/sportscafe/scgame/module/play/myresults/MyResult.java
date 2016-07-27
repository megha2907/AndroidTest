package in.sportscafe.scgame.module.play.myresults;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.module.play.prediction.dto.Question;

/**
 * Created by Jeeva on 5/7/16.
 */
public class MyResult {

    @JsonProperty("match_id")
    private Integer matchId;

    @JsonProperty("tournament_id")
    private Integer tournamentId;

    @JsonProperty("tournament_name")
    private String tournamentName;

    @JsonProperty("match_parties")
    private String matchParties;

    @JsonProperty("match_result")
    private String matchResult;

    @JsonProperty("match_starttime")
    private String matchStartTime;

    @JsonProperty("questions")
    private List<Question> questions = new ArrayList<>();

    /**
     * @return The matchId
     */
    @JsonProperty("match_id")
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * @param matchId The match_id
     */
    @JsonProperty("match_id")
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    /**
     * @return The tournamentId
     */
    @JsonProperty("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    /**
     * @param tournamentId The tournament_id
     */
    @JsonProperty("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * @return The tournamentName
     */
    @JsonProperty("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * @param tournamentName The tournament_name
     */
    @JsonProperty("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     * @return The matchParties
     */
    @JsonProperty("match_parties")
    public String getMatchParties() {
        return matchParties;
    }

    /**
     * @param matchParties The match_parties
     */
    @JsonProperty("match_parties")
    public void setMatchParties(String matchParties) {
        this.matchParties = matchParties;
    }

    /**
     * @return The matchResult
     */
    @JsonProperty("match_result")
    public String getMatchResult() {
        return matchResult;
    }

    /**
     * @param matchResult The match_result
     */
    @JsonProperty("match_result")
    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    /**
     * @return The matchStartTime
     */
    @JsonProperty("match_starttime")
    public String getMatchStartTime() {
        return matchStartTime;
    }

    /**
     * @param matchStartTime The match_starttime
     */
    @JsonProperty("match_starttime")
    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    /**
     * @return The questions
     */
    @JsonProperty("questions")
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * @param questions The questions
     */
    @JsonProperty("questions")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}