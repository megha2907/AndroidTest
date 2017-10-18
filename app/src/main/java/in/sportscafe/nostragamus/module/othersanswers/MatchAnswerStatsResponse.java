package in.sportscafe.nostragamus.module.othersanswers;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/01/17.
 */

public class MatchAnswerStatsResponse {

    @SerializedName("data")
    private List<MatchAnswerStats> questionAnswers = new ArrayList<>();

    @SerializedName("data")
    public List<MatchAnswerStats> getQuestionAnswers() {
        return questionAnswers;
    }

    @SerializedName("data")
    public void setQuestionAnswers(List<MatchAnswerStats> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}