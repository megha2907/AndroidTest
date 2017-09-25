package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 25/09/17.
 */

public class VerifyJoinContestRequest {

    @SerializedName("order_id")
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
