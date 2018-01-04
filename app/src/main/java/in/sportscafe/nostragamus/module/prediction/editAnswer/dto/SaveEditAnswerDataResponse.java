package in.sportscafe.nostragamus.module.prediction.editAnswer.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sc on 29/12/17.
 */

public class SaveEditAnswerDataResponse {

    @SerializedName("powerups_remaining")
    private PowerUp powerUp;

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }
}
