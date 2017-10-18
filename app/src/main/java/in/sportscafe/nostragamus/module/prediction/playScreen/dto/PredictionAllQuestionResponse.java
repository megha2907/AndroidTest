package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 18/09/17.
 */

public class PredictionAllQuestionResponse {

    @SerializedName("data")
    private List<PredictionQuestion> questions = null;

    @SerializedName("powerups")
    private PowerUp powerUp;

    @SerializedName("data")
    public List<PredictionQuestion> getQuestions() {
        return questions;
    }

    @SerializedName("data")
    public void setQuestions(List<PredictionQuestion> questions) {
        this.questions = questions;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }
}
