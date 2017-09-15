package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 5/17/17.
 */

public class ChangeAnswer {

    @SerializedName("question_id")
    private Integer questionId;

    @SerializedName("answer")
    private Integer answerId;

    public ChangeAnswer(Integer questionId, Integer answerId) {
        this.questionId = questionId;
        this.answerId = answerId;
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
}
