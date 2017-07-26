package in.sportscafe.nostragamus.module.play.powerup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 21/07/17.
 */

public class PowerUpBankStatusResponse implements Cloneable {

    @JsonProperty("maxTransferLimit")
    private int maxTransferLimit;

    @JsonProperty("alreadyTransferred")
    private AlreadyTransferredPowerupDto alreadyTransferred;

    @JsonProperty("userBalance")
    private UserBalancePowerupDto userBalance;


    /* Clones the object */
    public PowerUpBankStatusResponse clone() {
        try {
            PowerUpBankStatusResponse obj = (PowerUpBankStatusResponse)super.clone();
            obj.alreadyTransferred = (AlreadyTransferredPowerupDto) alreadyTransferred.clone();
            obj.userBalance = (UserBalancePowerupDto) userBalance.clone();
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
