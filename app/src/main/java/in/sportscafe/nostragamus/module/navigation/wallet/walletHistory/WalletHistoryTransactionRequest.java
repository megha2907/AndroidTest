package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletHistoryTransactionRequest {

    @SerializedName("user_id")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
