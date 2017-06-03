package in.sportscafe.nostragamus.module.appupdate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateDetails {

    @JsonProperty("slides")
    private List<AppUpdateDto> appUpdateSlides = new ArrayList<>();

    @JsonProperty("update_url")
    private String updateUrl;

    @JsonProperty("update_type")
    private String updateType;

    @JsonProperty("req_version")
    private int reqVersion;

    @JsonProperty("slides")
    public List<AppUpdateDto> getAppUpdateSlides() {
        return appUpdateSlides;
    }
    @JsonProperty("slides")
    public void setAppUpdateSlides(List<AppUpdateDto> appUpdateSlides) {
        this.appUpdateSlides = appUpdateSlides;
    }

    @JsonProperty("update_url")
    public String getUpdateUrl() {
        return updateUrl;
    }

    @JsonProperty("update_url")
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    @JsonProperty("req_version")
    public int getReqVersion() {
        return reqVersion;
    }

    @JsonProperty("req_version")
    public void setReqVersion(int reqVersion) {
        this.reqVersion = reqVersion;
    }

    @JsonProperty("update_type")
    public String getUpdateType() {
        return updateType;
    }

    @JsonProperty("update_type")
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

}
