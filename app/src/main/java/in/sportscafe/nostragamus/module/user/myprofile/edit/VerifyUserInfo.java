package in.sportscafe.nostragamus.module.user.myprofile.edit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 6/30/17.
 */

public class VerifyUserInfo {

    @SerializedName("user_name")
    private String userName;

    @SerializedName("wallet_init")
    private Integer walletInitialAmount;

    @SerializedName("user_name")
    public String getUserName() {
        return userName;
    }

    @SerializedName("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("wallet_init")
    public Integer getWalletInitialAmount() {
        return walletInitialAmount;
    }

    @SerializedName("wallet_init")
    public void setWalletInitialAmount(Integer walletInitialAmount) {
        this.walletInitialAmount = walletInitialAmount;
    }

}
