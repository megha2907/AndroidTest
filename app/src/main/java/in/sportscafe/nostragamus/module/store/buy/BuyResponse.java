package in.sportscafe.nostragamus.module.store.buy;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 27/07/17.
 */

public class BuyResponse {

    @JsonProperty("status")
    private int status;

    @JsonProperty("order_id")
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
