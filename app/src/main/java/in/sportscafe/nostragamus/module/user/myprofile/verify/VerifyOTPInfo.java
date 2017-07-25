package in.sportscafe.nostragamus.module.user.myprofile.verify;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 7/20/17.
 */

public class VerifyOTPInfo {

    @JsonProperty("otp_code")
    private Integer validOTPCode;

    @JsonProperty("otp_code")
    public Integer getValidOTPCode() {
        return validOTPCode;
    }

    @JsonProperty("otp_code")
    public void setValidOTPCode(Integer validOTPCode) {
        this.validOTPCode = validOTPCode;
    }

}
