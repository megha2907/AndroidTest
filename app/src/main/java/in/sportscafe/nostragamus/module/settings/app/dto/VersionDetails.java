package in.sportscafe.nostragamus.module.settings.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshu on 4/8/16.
 */
public class VersionDetails {

    @JsonProperty("forceUpdateVersion")
    private Version forceUpdateVersion;

    @JsonProperty("normalUpdateVersion")
    private Version normalUpdateVersion;

    @JsonProperty("paidForceUpdateVersion")
    private Version paidForceUpdateVersion;

    @JsonProperty("paidNormalUpdateVersion")
    private Version paidNormalUpdateVersion;

    @JsonProperty("feedback_text")
    private String feedbackText;

    @JsonProperty("download_paid_text")
    private String downloadPaidText;

    @JsonProperty("forceUpdateVersion")
    public Version getForceUpdateVersion() {
        return forceUpdateVersion;
    }

    @JsonProperty("forceUpdateVersion")
    public void setForceUpdateVersion(Version forceUpdateVersion) {
        this.forceUpdateVersion = forceUpdateVersion;
    }

    @JsonProperty("normalUpdateVersion")
    public Version getNormalUpdateVersion() {
        return normalUpdateVersion;
    }

    @JsonProperty("normalUpdateVersion")
    public void setNormalUpdateVersion(Version normalUpdateVersion) {
        this.normalUpdateVersion = normalUpdateVersion;
    }

    @JsonProperty("paidForceUpdateVersion")
    public Version getPaidForceUpdateVersion() {
        return paidForceUpdateVersion;
    }

    @JsonProperty("paidForceUpdateVersion")
    public void setPaidForceUpdateVersion(Version paidForceUpdateVersion) {
        this.paidForceUpdateVersion = paidForceUpdateVersion;
    }

    @JsonProperty("paidNormalUpdateVersion")
    public Version getPaidNormalUpdateVersion() {
        return paidNormalUpdateVersion;
    }

    @JsonProperty("paidNormalUpdateVersion")
    public void setPaidNormalUpdateVersion(Version paidNormalUpdateVersion) {
        this.paidNormalUpdateVersion = paidNormalUpdateVersion;
    }

    @JsonProperty("feedback_text")
    public String getFeedbackText() {
        return feedbackText;
    }

    @JsonProperty("feedback_text")
    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    @JsonProperty("download_paid_text")
    public String getDownloadPaidText() {
        return downloadPaidText;
    }

    @JsonProperty("download_paid_text")
    public void setDownloadPaidText(String downloadPaidText) {
        this.downloadPaidText = downloadPaidText;
    }
}