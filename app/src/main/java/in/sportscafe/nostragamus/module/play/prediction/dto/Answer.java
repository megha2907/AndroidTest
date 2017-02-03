package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("powerup_id")
    private String powerUpId;


    public Answer() {
    }

    public Answer(Integer matchId, Integer questionId, Integer answerId, String answerTime, String powerUpId) {
        this.matchId = matchId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.answerTime = answerTime;
        this.powerUpId = powerUpId;
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

    @JsonProperty("powerup_id")
    public String getPowerUpId() {
        return powerUpId;
    }

    @JsonProperty("powerup_id")
    public void setPowerUpId(String powerUpId) {
        this.powerUpId = powerUpId;
    }
}