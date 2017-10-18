package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 12/06/17.
 */

public class WalletPaytmDto {

    @SerializedName("mobileNumber")
    private String mobileNumber;

    @SerializedName("mobileNumber")
    public String getMobileNumber() {
        return mobileNumber;
    }

    @SerializedName("mobileNumber")
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
