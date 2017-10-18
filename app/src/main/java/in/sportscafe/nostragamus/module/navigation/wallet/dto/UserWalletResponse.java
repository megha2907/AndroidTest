package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;

/**
 * Created by sandip on 12/06/17.
 */

public class UserWalletResponse {

    @SerializedName("winningsAmount")
    private double winningsAmount;

    @SerializedName("depositAmount")
    private double depositAmount;

    @SerializedName("promoAmount")
    private double promoAmount;

    @SerializedName("withdrawalProgress")
    private int withdrawalProgress;

    @SerializedName("lockedAmount")
    private double lockedAmount;

    @SerializedName("payment_info")
    private UserPaymentInfo userPaymentInfo;

    @SerializedName("winningsAmount")
    public double getWinningsAmount() {
        return winningsAmount;
    }

    @SerializedName("winningsAmount")
    public void setWinningsAmount(double winningsAmount) {
        this.winningsAmount = winningsAmount;
    }

    @SerializedName("depositAmount")
    public double getDepositAmount() {
        return depositAmount;
    }

    @SerializedName("depositAmount")
    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    @SerializedName("promoAmount")
    public double getPromoAmount() {
        return promoAmount;
    }

    @SerializedName("promoAmount")
    public void setPromoAmount(double promoAmount) {
        this.promoAmount = promoAmount;
    }

    @SerializedName("withdrawalProgress")
    public int getWithdrawalProgress() {
        return withdrawalProgress;
    }

    @SerializedName("withdrawalProgress")
    public void setWithdrawalProgress(int withdrawalProgress) {
        this.withdrawalProgress = withdrawalProgress;
    }

    public double getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(double lockedAmount) {
        this.lockedAmount = lockedAmount;
    }

    @SerializedName("payment_info")
    public UserPaymentInfo getUserPaymentInfo() {
        return userPaymentInfo;
    }

    @SerializedName("payment_info")
    public void setUserPaymentInfo(UserPaymentInfo userPaymentInfo) {
        this.userPaymentInfo = userPaymentInfo;
    }
}
