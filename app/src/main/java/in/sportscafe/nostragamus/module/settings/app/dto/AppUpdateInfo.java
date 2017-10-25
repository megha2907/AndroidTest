package in.sportscafe.nostragamus.module.settings.app.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.utils.CodeSnippet;

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
    private String reqVersion;

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
    private String getReqVersion() {
        return reqVersion;
    }

    @SerializedName("req_version")
    public void setReqVersion(String reqVersion) {
        this.reqVersion = reqVersion;
    }

    /**
     * Always use this method to get requested version code, as API response for this param comes as String
     * This method converts string to int
     * @return - request version code
     */
    public int getUpdateRequestVersion() {
        return CodeSnippet.convertStringToInt(getReqVersion());
    }
}
