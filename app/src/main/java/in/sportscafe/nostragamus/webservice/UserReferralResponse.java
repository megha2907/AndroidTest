package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.settings.app.dto.SettingsDetails;

/**
 * Created by deepanshi on 6/23/17.
 */

public class UserReferralResponse {

    @JsonProperty("refer_friend")
    private UserReferralInfo userReferralInfo;

    @JsonProperty("refer_friend")
    public UserReferralInfo getUserReferralInfo() {
        return userReferralInfo;
    }

    @JsonProperty("refer_friend")
    public void setUserReferralInfo(UserReferralInfo userReferralInfo) {
        this.userReferralInfo = userReferralInfo;
    }


}
