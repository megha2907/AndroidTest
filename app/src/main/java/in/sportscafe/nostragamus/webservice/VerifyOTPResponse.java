package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.myprofile.verify.VerifyOTPInfo;

/**
 * Created by deepanshi on 7/20/17.
 */

public class VerifyOTPResponse {

    @JsonProperty("data")
    private VerifyOTPInfo verifyOTPInfo;

    @JsonProperty("data")
    public VerifyOTPInfo getVerifyOTPInfo() {
        return verifyOTPInfo;
    }

    @JsonProperty("data")
    public void setVerifyOTPInfo(VerifyOTPInfo verifyOTPInfo) {
        this.verifyOTPInfo = verifyOTPInfo;
    }

}
