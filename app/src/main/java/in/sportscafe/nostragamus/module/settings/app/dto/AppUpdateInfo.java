package in.sportscafe.nostragamus.module.settings.app.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 6/8/17.
 */
@Parcel
public class AppUpdateInfo {

    @SerializedName("update_url")
    private String updateUrl;

    @SerializedName("update_type")
    private String updateType;

    @SerializedName("req_version")
    private int reqVersion;

    @SerializedName("update_url")
    public String getUpdateUrl() {
        return updateUrl;
    }

    @SerializedName("update_url")
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    @SerializedName("update_type")
    public String getUpdateType() {
        return updateType;
    }

    @SerializedName("update_type")
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    @SerializedName("req_version")
    public int getReqVersion() {
        return reqVersion;
    }

    @SerializedName("req_version")
    public void setReqVersion(int reqVersion) {
        this.reqVersion = reqVersion;
    }

}
