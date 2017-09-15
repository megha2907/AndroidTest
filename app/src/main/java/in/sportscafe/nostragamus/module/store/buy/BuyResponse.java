package in.sportscafe.nostragamus.module.store.buy;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 27/07/17.
 */

public class BuyResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("order_id")
    private String orderId;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
