package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Jeeva on 15/6/16.
 */
public class Answer {

    @SerializedName("match_id")
    private Integer matchId;

    @SerializedName("question_id")
    private Integer questionId;

    @SerializedName("answer")
    private Integer answerId;

    @SerializedName("answer_time")
    private String answerTime;

    /*@SerializedName("powerup_id")
    private String powerUpId;*/

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("powerup_id_arr")
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


    @SerializedName("match_id")
    public Integer getMatchId() {
        return matchId;
    }

    @SerializedName("match_id")
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    @SerializedName("question_id")

    public Integer getQuestionId() {
        return questionId;
    }

    @SerializedName("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @SerializedName("answer")
    public Integer getAnswerId() {
        return answerId;
    }

    @SerializedName("answer")
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    @SerializedName("answer_time")
    public String getAnswerTime() {
        return answerTime;
    }

    @SerializedName("answer_time")
    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    /*@SerializedName("powerup_id")
    public String getPowerUpArrayList() {
        return powerUpId;
    }

    @SerializedName("powerup_id")
    public void setPowerUpArrayList(String powerUpId) {
        this.powerUpId = powerUpId;
    }*/

    @SerializedName("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("powerup_id_arr")
    public ArrayList<String> getPowerupsArray() {
        return powerupsArray;
    }

    @SerializedName("powerup_id_arr")
    public void setPowerupsArray(ArrayList<String> powerupsArray) {
        this.powerupsArray = powerupsArray;
    }
}