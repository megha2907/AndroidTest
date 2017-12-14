package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 28/11/17.
 */

public class AddMoneyPaymentCouponResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("order_id")
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
