package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 10/04/17.
 */

public class AddUserPaymentPaytmRequest {

    @SerializedName("payment_mode")
    private String paymentMode;

    @SerializedName("mobile")
    private String mobile;

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
