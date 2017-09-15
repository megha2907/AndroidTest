package in.sportscafe.nostragamus.module.navigation.appupdate;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateResponse {

    @SerializedName("app")
    private AppUpdateDetails appUpdateDetails;

    @SerializedName("app")
    public AppUpdateDetails getAppUpdateDetails() {
        return appUpdateDetails;
    }

    @SerializedName("app")
    public void setAppUpdateDetails(AppUpdateDetails appUpdateDetails) {
        this.appUpdateDetails = appUpdateDetails;
    }

}
