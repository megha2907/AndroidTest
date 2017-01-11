package in.sportscafe.nostragamus.module.othersanswers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/01/17.
 */

public class PlayerResultPercentageResponse {

    @JsonProperty("data")
    private List<AnswerPercentage> questionAnswers = new ArrayList<>();

    @JsonProperty("data")
    public List<AnswerPercentage> getQuestionAnswers() {
        return questionAnswers;
    }

    @JsonProperty("data")
    public void setQuestionAnswers(List<AnswerPercentage> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}