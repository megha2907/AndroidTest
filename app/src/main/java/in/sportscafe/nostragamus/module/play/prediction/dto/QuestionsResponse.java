package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class QuestionsResponse {

    @SerializedName("data")
    private List<Question> questions = new ArrayList<>();

    @SerializedName("data")
    public List<Question> getQuestions() {
        return questions;
    }

    @SerializedName("data")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}