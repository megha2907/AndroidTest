package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 07/10/17.
 */

public class TransferPowerUpFromBankResponseData {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("powerups")
    private PowerUp balancePowerUp;

    @SerializedName("powerups_from_bank")
    private PowerUp transferredFromBankPowerUp;

    @SerializedName("add_powerups")
    private PowerUp powerUpsAdded;

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

    public PowerUp getPowerUpsAdded() {
        return powerUpsAdded;
    }

    public void setPowerUpsAdded(PowerUp powerUpsAdded) {
        this.powerUpsAdded = powerUpsAdded;
    }
}
