package in.sportscafe.nostragamus.module.navigation.appupdate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateResponse {

    @JsonProperty("app")
    private AppUpdateDetails appUpdateDetails;

    @JsonProperty("app")
    public AppUpdateDetails getAppUpdateDetails() {
        return appUpdateDetails;
    }

    @JsonProperty("app")
    public void setAppUpdateDetails(AppUpdateDetails appUpdateDetails) {
        this.appUpdateDetails = appUpdateDetails;
    }

}
