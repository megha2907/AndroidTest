package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.settings.app.dto.SettingsDetails;

/**
 * Created by deepanshi on 6/23/17.
 */


public class UserReferralResponse {

    @SerializedName("refer_friend")
    private UserReferralInfo userReferralInfo;

    @SerializedName("refer_friend")
    public UserReferralInfo getUserReferralInfo() {
        return userReferralInfo;
    }

    @SerializedName("refer_friend")
    public void setUserReferralInfo(UserReferralInfo userReferralInfo) {
        this.userReferralInfo = userReferralInfo;
    }


}
