package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 19/09/17.
 */

public class AnswerResponse {

    @SerializedName("powerups_remaining")
    private PowerUp powerUp;

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }
}
