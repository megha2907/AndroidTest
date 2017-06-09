package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletTransactionRequest {

    @JsonProperty("user_id")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
