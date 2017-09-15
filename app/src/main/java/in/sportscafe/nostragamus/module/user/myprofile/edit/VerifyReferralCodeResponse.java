package in.sportscafe.nostragamus.module.user.myprofile.edit;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 6/30/17.
 */

public class VerifyReferralCodeResponse {

    @SerializedName("data")
    private VerifyUserInfo verifyUserInfo;

    @SerializedName("data")
    public VerifyUserInfo getVerifyUserInfo() {
        return verifyUserInfo;
    }

    @SerializedName("data")
    public void setVerifyUserInfo(VerifyUserInfo verifyUserInfo) {
        this.verifyUserInfo = verifyUserInfo;
    }

}
