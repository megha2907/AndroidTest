package in.sportscafe.nostragamus.module.othersanswers;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 11/01/17.
 */

public class AnswerPercentage {

    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("answer3")
    private Integer answer3;

    @JsonProperty("answer1")
    private Integer answer1;

    @JsonProperty("answer2")
    private Integer answer2;

    @JsonProperty("question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    @JsonProperty("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @JsonProperty("answer3")
    public Integer getAnswer3() {
        return answer3;
    }

    @JsonProperty("answer3")
    public void setAnswer3(Integer answer3) {
        this.answer3 = answer3;
    }

    @JsonProperty("answer1")
    public Integer getAnswer1() {
        return answer1;
    }

    @JsonProperty("answer1")
    public void setAnswer1(Integer answer1) {
        this.answer1 = answer1;
    }

    @JsonProperty("answer2")
    public Integer getAnswer2() {
        return answer2;
    }

    @JsonProperty("answer2")
    public void setAnswer2(Integer answer2) {
        this.answer2 = answer2;
    }
}