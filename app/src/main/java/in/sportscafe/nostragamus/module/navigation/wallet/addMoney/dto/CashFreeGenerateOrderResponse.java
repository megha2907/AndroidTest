package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/14/18.
 */
@Parcel
public class CashFreeGenerateOrderResponse {

    @SerializedName("CALLBACK_URL")
    private String callbackUrl;

    @SerializedName("ORDER_ID")
    private String orderId;

    @SerializedName("ORDER_AMOUNT")
    private int orderAmount;

    @SerializedName("ORDER_NOTE")
    private String orderNote;

    @SerializedName("CUSTOMER_NAME")
    private String customerName;

    @SerializedName("CUSTOMER_PHONE")
    private String customerPhone;

    @SerializedName("CUSTOMER_EMAIL")
    private String customerEmail;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

}
