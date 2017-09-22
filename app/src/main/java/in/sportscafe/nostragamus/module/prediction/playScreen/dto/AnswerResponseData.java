package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 22/09/17.
 */

public class AnswerResponseData {

    @SerializedName("powerups_remaining")
    private PowerUp powerUp;

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

}
