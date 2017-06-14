package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 12/06/17.
 */

public class WalletPaytmDto {

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("mobileNumber")
    public String getMobileNumber() {
        return mobileNumber;
    }

    @JsonProperty("mobileNumber")
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
