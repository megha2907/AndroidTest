package in.sportscafe.nostragamus.module.othersanswers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/01/17.
 */

public class MatchAnswerStatsResponse {

    @JsonProperty("data")
    private List<MatchAnswerStats> questionAnswers = new ArrayList<>();

    @JsonProperty("data")
    public List<MatchAnswerStats> getQuestionAnswers() {
        return questionAnswers;
    }

    @JsonProperty("data")
    public void setQuestionAnswers(List<MatchAnswerStats> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}