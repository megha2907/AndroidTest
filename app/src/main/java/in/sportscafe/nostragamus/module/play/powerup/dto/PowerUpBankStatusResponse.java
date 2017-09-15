package in.sportscafe.nostragamus.module.play.powerup.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 21/07/17.
 */

public class PowerUpBankStatusResponse implements Cloneable {

    @SerializedName("maxTransferLimit")
    private int maxTransferLimit;

    @SerializedName("alreadyTransferred")
    private AlreadyTransferredPowerupDto alreadyTransferred;

    @SerializedName("userBalance")
    private UserBalancePowerupDto userBalance;


    /* Clones the object */
    public PowerUpBankStatusResponse clone() {
        try {
            PowerUpBankStatusResponse obj = (PowerUpBankStatusResponse)super.clone();
            obj.alreadyTransferred = alreadyTransferred.clone();
            obj.userBalance = userBalance.clone();
            return obj;
        } catch (CloneNotSupportedException e) {}
        return null;
    }


    public int getMaxTransferLimit() {
        return maxTransferLimit;
    }

    public void setMaxTransferLimit(int maxTransferLimit) {
        this.maxTransferLimit = maxTransferLimit;
    }

    public AlreadyTransferredPowerupDto getAlreadyTransferred() {
        return alreadyTransferred;
    }

    public void setAlreadyTransferred(AlreadyTransferredPowerupDto alreadyTransferred) {
        this.alreadyTransferred = alreadyTransferred;
    }

    public UserBalancePowerupDto getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(UserBalancePowerupDto userBalance) {
        this.userBalance = userBalance;
    }

}
