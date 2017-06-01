package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Jeeva on 15/6/16.
 */
public class Answer {

    @JsonProperty("match_id")
    private Integer matchId;

    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("answer")
    private Integer answerId;

    @JsonProperty("answer_time")
    private String answerTime;

    /*@JsonProperty("powerup_id")
    private String powerUpId;*/

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("powerup_id_arr")
    private ArrayList<String> powerupsArray;

    public Answer() {
    }

    public Answer(Integer matchId, Integer questionId,
                  Integer answerId, String answerTime,
                  ArrayList<String> powerUpArray, int challengeId) {
        this.matchId = matchId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.answerTime = answerTime;
        this.powerupsArray = powerUpArray;
        this.challengeId = challengeId;
    }


    @JsonProperty("match_id")
    public Integer getMatchId() {
        return matchId;
    }

    @JsonProperty("match_id")
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    @JsonProperty("question_id")

    public Integer getQuestionId() {
        return questionId;
    }

    @JsonProperty("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @JsonProperty("answer")
    public Integer getAnswerId() {
        return answerId;
    }

    @JsonProperty("answer")
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    @JsonProperty("answer_time")
    public String getAnswerTime() {
        return answerTime;
    }

    @JsonProperty("answer_time")
    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    /*@JsonProperty("powerup_id")
    public String getPowerUpArrayList() {
        return powerUpId;
    }

    @JsonProperty("powerup_id")
    public void setPowerUpArrayList(String powerUpId) {
        this.powerUpId = powerUpId;
    }*/

    @JsonProperty("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("powerup_id_arr")
    public ArrayList<String> getPowerupsArray() {
        return powerupsArray;
    }

    @JsonProperty("powerup_id_arr")
    public void setPowerupsArray(ArrayList<String> powerupsArray) {
        this.powerupsArray = powerupsArray;
    }
}