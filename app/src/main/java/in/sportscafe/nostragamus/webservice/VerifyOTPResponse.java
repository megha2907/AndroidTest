package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.myprofile.verify.VerifyOTPInfo;

/**
 * Created by deepanshi on 7/20/17.
 */

public class VerifyOTPResponse {

    @SerializedName("data")
    private VerifyOTPInfo verifyOTPInfo;

    @SerializedName("data")
    public VerifyOTPInfo getVerifyOTPInfo() {
        return verifyOTPInfo;
    }

    @SerializedName("data")
    public void setVerifyOTPInfo(VerifyOTPInfo verifyOTPInfo) {
        this.verifyOTPInfo = verifyOTPInfo;
    }

}
