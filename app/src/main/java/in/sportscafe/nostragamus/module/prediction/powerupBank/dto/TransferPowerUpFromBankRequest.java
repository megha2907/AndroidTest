package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 21/09/17.
 */

public class TransferPowerUpFromBankRequest {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("add_powerups")
    private PowerUp powerUps;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public PowerUp getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(PowerUp powerUps) {
        this.powerUps = powerUps;
    }
}
