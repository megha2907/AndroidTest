package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 11/25/16.
 */

public class AudiencePollRequest {

    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    @JsonProperty("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}