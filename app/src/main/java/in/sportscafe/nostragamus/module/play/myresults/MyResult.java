package in.sportscafe.nostragamus.module.play.myresults;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.play.prediction.dto.Question;

/**
 * Created by Jeeva on 5/7/16.
 */
public class MyResult {

    @SerializedName("match_id")
    private Integer matchId;

    @SerializedName("tournament_id")
    private Integer tournamentId;

    @SerializedName("tournament_name")
    private String tournamentName;

    @SerializedName("match_parties")
    private String matchParties;

    @SerializedName("match_result")
    private String matchResult;

    @SerializedName("match_starttime")
    private String matchStartTime;

    @SerializedName("questions")
    private List<Question> questions = new ArrayList<>();

    /**
     * @return The matchId
     */
    @SerializedName("match_id")
    public Integer getMatchId() {
        return matchId;
    }

    /**
     * @param matchId The match_id
     */
    @SerializedName("match_id")
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    /**
     * @return The tournamentId
     */
    @SerializedName("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    /**
     * @param tournamentId The tournament_id
     */
    @SerializedName("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * @return The tournamentName
     */
    @SerializedName("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * @param tournamentName The tournament_name
     */
    @SerializedName("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     * @return The matchParties
     */
    @SerializedName("match_parties")
    public String getMatchParties() {
        return matchParties;
    }

    /**
     * @param matchParties The match_parties
     */
    @SerializedName("match_parties")
    public void setMatchParties(String matchParties) {
        this.matchParties = matchParties;
    }

    /**
     * @return The matchResult
     */
    @SerializedName("match_result")
    public String getMatchResult() {
        return matchResult;
    }

    /**
     * @param matchResult The match_result
     */
    @SerializedName("match_result")
    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    /**
     * @return The matchStartTime
     */
    @SerializedName("match_starttime")
    public String getMatchStartTime() {
        return matchStartTime;
    }

    /**
     * @param matchStartTime The match_starttime
     */
    @SerializedName("match_starttime")
    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    /**
     * @return The questions
     */
    @SerializedName("questions")
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * @param questions The questions
     */
    @SerializedName("questions")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}