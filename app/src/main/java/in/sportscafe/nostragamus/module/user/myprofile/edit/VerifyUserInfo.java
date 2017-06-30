package in.sportscafe.nostragamus.module.user.myprofile.edit;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 6/30/17.
 */

public class VerifyUserInfo {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("wallet_init")
    private Integer walletInitialAmount;

    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("wallet_init")
    public Integer getWalletInitialAmount() {
        return walletInitialAmount;
    }

    @JsonProperty("wallet_init")
    public void setWalletInitialAmount(Integer walletInitialAmount) {
        this.walletInitialAmount = walletInitialAmount;
    }

}
