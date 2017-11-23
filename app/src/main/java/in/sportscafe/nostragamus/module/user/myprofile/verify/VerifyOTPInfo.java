package in.sportscafe.nostragamus.module.user.myprofile.verify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 7/20/17.
 */

public class VerifyOTPInfo {

    @SerializedName("otp_code")
    private Integer validOTPCode;

    @SerializedName("wallet_balance")
    private int walletBalance;

    @SerializedName("otp_code")
    public Integer getValidOTPCode() {
        return validOTPCode;
    }

    @SerializedName("otp_code")
    public void setValidOTPCode(Integer validOTPCode) {
        this.validOTPCode = validOTPCode;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        this.walletBalance = walletBalance;
    }

}
