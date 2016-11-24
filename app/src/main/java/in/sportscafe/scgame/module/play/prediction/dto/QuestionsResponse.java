package in.sportscafe.scgame.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class QuestionsResponse {

    @JsonProperty("data")
    private List<Question> questions = new ArrayList<>();

    @JsonProperty("data")
    public List<Question> getQuestions() {
        return questions;
    }

    @JsonProperty("data")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}