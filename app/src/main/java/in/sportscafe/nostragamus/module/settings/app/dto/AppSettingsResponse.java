package in.sportscafe.nostragamus.module.settings.app.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 3/10/16.
 */

public class AppSettingsResponse {

    @SerializedName("settings")
    private SettingsDetails settingsDetails;

    @SerializedName("settings")
    public SettingsDetails getSettingsDetails() {
        return settingsDetails;
    }

    @SerializedName("settings")
    public void setSettingsDetails(SettingsDetails settingsDetails) {
        this.settingsDetails = settingsDetails;
    }

    @SerializedName("update")
    private AppUpdateInfo appUpdateInfo;

    @SerializedName("update")
    public AppUpdateInfo getAppUpdateInfo() {
        return appUpdateInfo;
    }

    @SerializedName("update")
    public void setAppUpdateInfo(AppUpdateInfo appUpdateInfo) {
        this.appUpdateInfo = appUpdateInfo;
    }

}