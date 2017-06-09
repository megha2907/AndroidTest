package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 10/04/17.
 */

public class AddUserPaymentPaytmRequest {

    @JsonProperty("payment_mode")
    private String paymentMode;

    @JsonProperty("mobile")
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
