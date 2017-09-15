package in.sportscafe.nostragamus.module.navigation.appupdate;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateDetails {

    @SerializedName("slides")
    private List<AppUpdateDto> appUpdateSlides = new ArrayList<>();

    @SerializedName("update_url")
    private String updateUrl;

    @SerializedName("update_type")
    private String updateType;

    @SerializedName("req_version")
    private int reqVersion;

    @SerializedName("slides")
    public List<AppUpdateDto> getAppUpdateSlides() {
        return appUpdateSlides;
    }
    @SerializedName("slides")
    public void setAppUpdateSlides(List<AppUpdateDto> appUpdateSlides) {
        this.appUpdateSlides = appUpdateSlides;
    }

    @SerializedName("update_url")
    public String getUpdateUrl() {
        return updateUrl;
    }

    @SerializedName("update_url")
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    @SerializedName("req_version")
    public int getReqVersion() {
        return reqVersion;
    }

    @SerializedName("req_version")
    public void setReqVersion(int reqVersion) {
        this.reqVersion = reqVersion;
    }

    @SerializedName("update_type")
    public String getUpdateType() {
        return updateType;
    }

    @SerializedName("update_type")
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

}
