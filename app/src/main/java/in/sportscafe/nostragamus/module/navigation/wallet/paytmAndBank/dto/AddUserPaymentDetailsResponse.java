package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 10/04/17.
 */

public class AddUserPaymentDetailsResponse {

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
