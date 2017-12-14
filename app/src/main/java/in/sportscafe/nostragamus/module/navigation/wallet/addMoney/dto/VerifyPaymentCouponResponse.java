package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 13/12/17.
 */

public class VerifyPaymentCouponResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("try-again")
    private boolean tryAgain = false;

    @SerializedName("money_added")
    private int moneyAdded;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isTryAgain() {
        return tryAgain;
    }

    public void setTryAgain(boolean tryAgain) {
        this.tryAgain = tryAgain;
    }

    public int getMoneyAdded() {
        return moneyAdded;
    }

    public void setMoneyAdded(int moneyAdded) {
        this.moneyAdded = moneyAdded;
    }
}
