package in.sportscafe.nostragamus.module.settings.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 6/8/17.
 */
@Parcel
public class AppUpdateInfo {

    @JsonProperty("update_url")
    private String updateUrl;

    @JsonProperty("update_type")
    private String updateType;

    @JsonProperty("req_version")
    private int reqVersion;

    @JsonProperty("update_url")
    public String getUpdateUrl() {
        return updateUrl;
    }

    @JsonProperty("update_url")
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    @JsonProperty("update_type")
    public String getUpdateType() {
        return updateType;
    }

    @JsonProperty("update_type")
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    @JsonProperty("req_version")
    public int getReqVersion() {
        return reqVersion;
    }

    @JsonProperty("req_version")
    public void setReqVersion(int reqVersion) {
        this.reqVersion = reqVersion;
    }

}
