package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 28/11/17.
 */

public class AddMoneyPaymentCouponResponse {

    @SerializedName("money_added")
    private int moneyAdded;

    public int getMoneyAdded() {
        return moneyAdded;
    }

    public void setMoneyAdded(int moneyAdded) {
        this.moneyAdded = moneyAdded;
    }
}
