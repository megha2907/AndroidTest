package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;

/**
 * Created by sandip on 12/06/17.
 */

public class UserWalletResponse {

    @JsonProperty("winningsAmount")
    private double winningsAmount;

    @JsonProperty("depositAmount")
    private double depositAmount;

    @JsonProperty("promoAmount")
    private double promoAmount;

    @JsonProperty("withdrawalProgress")
    private int withdrawalProgress;

    @JsonProperty("lockedAmount")
    private double lockedAmount;

    @JsonProperty("payment_info")
    private UserPaymentInfo userPaymentInfo;

    @JsonProperty("winningsAmount")
    public double getWinningsAmount() {
        return winningsAmount;
    }

    @JsonProperty("winningsAmount")
    public void setWinningsAmount(double winningsAmount) {
        this.winningsAmount = winningsAmount;
    }

    @JsonProperty("depositAmount")
    public double getDepositAmount() {
        return depositAmount;
    }

    @JsonProperty("depositAmount")
    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    @JsonProperty("promoAmount")
    public double getPromoAmount() {
        return promoAmount;
    }

    @JsonProperty("promoAmount")
    public void setPromoAmount(double promoAmount) {
        this.promoAmount = promoAmount;
    }

    @JsonProperty("withdrawalProgress")
    public int getWithdrawalProgress() {
        return withdrawalProgress;
    }

    @JsonProperty("withdrawalProgress")
    public void setWithdrawalProgress(int withdrawalProgress) {
        this.withdrawalProgress = withdrawalProgress;
    }

    public double getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(double lockedAmount) {
        this.lockedAmount = lockedAmount;
    }

    @JsonProperty("payment_info")
    public UserPaymentInfo getUserPaymentInfo() {
        return userPaymentInfo;
    }

    @JsonProperty("payment_info")
    public void setUserPaymentInfo(UserPaymentInfo userPaymentInfo) {
        this.userPaymentInfo = userPaymentInfo;
    }
}
