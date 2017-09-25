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
    private PowerUp balancePowerUp;

    @SerializedName("powerups_from_bank")
    private PowerUp transferredFromBankPowerUp;

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

    public PowerUp getBalancePowerUp() {
        return balancePowerUp;
    }

    public void setBalancePowerUp(PowerUp balancePowerUp) {
        this.balancePowerUp = balancePowerUp;
    }

    public PowerUp getTransferredFromBankPowerUp() {
        return transferredFromBankPowerUp;
    }

    public void setTransferredFromBankPowerUp(PowerUp transferredFromBankPowerUp) {
        this.transferredFromBankPowerUp = transferredFromBankPowerUp;
    }
}
