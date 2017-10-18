package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 21/07/17.
 */

public class PowerUpBankStatusResponse {

    @SerializedName("maxTransferLimit")
    private int maxTransferLimit;

    @SerializedName("alreadyTransferred")
    private PowerUp alreadyTransferred;

    @SerializedName("userBalance")
    private PowerUp userBalance;

    public int getMaxTransferLimit() {
        return maxTransferLimit;
    }

    public void setMaxTransferLimit(int maxTransferLimit) {
        this.maxTransferLimit = maxTransferLimit;
    }

    public PowerUp getAlreadyTransferred() {
        return alreadyTransferred;
    }

    public void setAlreadyTransferred(PowerUp alreadyTransferred) {
        this.alreadyTransferred = alreadyTransferred;
    }

    public PowerUp getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(PowerUp userBalance) {
        this.userBalance = userBalance;
    }

}
