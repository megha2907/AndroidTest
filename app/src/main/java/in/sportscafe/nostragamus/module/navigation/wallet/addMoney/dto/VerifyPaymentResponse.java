package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by deepanshi on 2/28/18.
 */

public class VerifyPaymentResponse {

    @SerializedName("status")
    private String paymentStatus;

    @SerializedName("status_code")
    private int paymentStatusCode;

    @SerializedName("transaction_amount")
    private int transactionAmount;

    @SerializedName("order_id")
    private String orderId;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentStatusCode() {
        return paymentStatusCode;
    }

    public void setPaymentStatusCode(int paymentStatusCode) {
        this.paymentStatusCode = paymentStatusCode;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


}
