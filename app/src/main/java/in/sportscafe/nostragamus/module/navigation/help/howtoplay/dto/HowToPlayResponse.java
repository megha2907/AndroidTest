package in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateDetails;

/**
 * Created by deepanshi on 2/22/18.
 */

public class HowToPlayResponse {

    @SerializedName("data")
    private HowToPlayDetails howToPlayDetails;

    public HowToPlayDetails getHowToPlayDetails() {
        return howToPlayDetails;
    }

    public void setHowToPlayDetails(HowToPlayDetails howToPlayDetails) {
        this.howToPlayDetails = howToPlayDetails;
    }
}
