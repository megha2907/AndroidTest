package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 11/24/16.
 */

public class AudiencePoll {

    @JsonProperty("answer")
    private Integer answerId;

    @JsonProperty("percentage")
    private String answerPercentage;

    @JsonProperty("answer")
    public Integer getAnswerId() {
        return answerId;
    }

    @JsonProperty("answer")
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    @JsonProperty("percentage")
    public String getAnswerPercentage() {
        return answerPercentage;
    }

    @JsonProperty("percentage")
    public void setAnswerPercentage(String answerPercentage) {
        this.answerPercentage = answerPercentage;
    }

}