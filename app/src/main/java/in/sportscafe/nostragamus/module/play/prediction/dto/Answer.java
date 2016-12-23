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
    private String answerId;

    @JsonProperty("answer_time")
    private String answerTime;

    @JsonProperty("powerup_id")
    private String powerUpId;


    public Answer() {
    }

    public Answer(Integer matchId, Integer questionId, String answerId, String answerTime, String powerUpId) {
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
    public String getAnswerId() {
        return answerId;
    }

    @JsonProperty("answer")
    public void setAnswerId(String answerId) {
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



}