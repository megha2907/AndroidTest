package in.sportscafe.nostragamus.module.prediction.editAnswer.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerPowerUpDto;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sc on 29/12/17.
 */

/**
 * Mostly same response as getAllQuestions to play
 */
public class QuestionForEditAnswerResponse {

    @SerializedName("question")
    private EditAnswerQuestion question;

    @SerializedName("powerups")
    private PowerUp powerUp;

    @SerializedName("appliedPowerups")
    private AnswerPowerUpDto appliedPowerUps;

    public EditAnswerQuestion getQuestion() {
        return question;
    }

    public void setQuestion(EditAnswerQuestion question) {
        this.question = question;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public AnswerPowerUpDto getAppliedPowerUps() {
        return appliedPowerUps;
    }

    public void setAppliedPowerUps(AnswerPowerUpDto appliedPowerUps) {
        this.appliedPowerUps = appliedPowerUps;
    }
}
