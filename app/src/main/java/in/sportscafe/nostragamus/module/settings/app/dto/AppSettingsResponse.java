package in.sportscafe.nostragamus.module.settings.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.appupdate.AppUpdateDetails;

/**
 * Created by Jeeva on 3/10/16.
 */

public class AppSettingsResponse {

    @JsonProperty("settings")
    private SettingsDetails settingsDetails;

    @JsonProperty("settings")
    public SettingsDetails getSettingsDetails() {
        return settingsDetails;
    }

    @JsonProperty("settings")
    public void setSettingsDetails(SettingsDetails settingsDetails) {
        this.settingsDetails = settingsDetails;
    }

    @JsonProperty("update")
    private AppUpdateInfo appUpdateInfo;

    @JsonProperty("update")
    public AppUpdateInfo getAppUpdateInfo() {
        return appUpdateInfo;
    }

    @JsonProperty("update")
    public void setAppUpdateInfo(AppUpdateInfo appUpdateInfo) {
        this.appUpdateInfo = appUpdateInfo;
    }

}