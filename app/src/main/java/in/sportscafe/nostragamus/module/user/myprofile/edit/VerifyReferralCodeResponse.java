package in.sportscafe.nostragamus.module.user.myprofile.edit;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 6/30/17.
 */

public class VerifyReferralCodeResponse {

    @JsonProperty("data")
    private VerifyUserInfo verifyUserInfo;

    @JsonProperty("data")
    public VerifyUserInfo getVerifyUserInfo() {
        return verifyUserInfo;
    }

    @JsonProperty("data")
    public void setVerifyUserInfo(VerifyUserInfo verifyUserInfo) {
        this.verifyUserInfo = verifyUserInfo;
    }

}
