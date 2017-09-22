package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;


/**
 * Created by sandip on 21/09/17.
 */

public class TransferPowerUpFromBankResponse {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("powerups")
    private PowerUp powerUp;

    @SerializedName("powerups_from_bank")
    private PowerUp powerUpFromBank;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public PowerUp getPowerUpFromBank() {
        return powerUpFromBank;
    }

    public void setPowerUpFromBank(PowerUp powerUpFromBank) {
        this.powerUpFromBank = powerUpFromBank;
    }
}
