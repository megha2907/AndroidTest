package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 5/17/17.
 */

public class ChangeAnswer {

    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("answer")
    private Integer answerId;

    public ChangeAnswer(Integer questionId, Integer answerId) {
        this.questionId = questionId;
        this.answerId = answerId;
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
}
