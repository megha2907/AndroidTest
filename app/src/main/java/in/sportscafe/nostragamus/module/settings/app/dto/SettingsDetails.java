package in.sportscafe.nostragamus.module.settings.app.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshu on 4/8/16.
 */
public class SettingsDetails {

    @SerializedName("forceUpdateVersion")
    private Version forceUpdateVersion;

    @SerializedName("normalUpdateVersion")
    private Version normalUpdateVersion;

    @SerializedName("paidForceUpdateVersion")
    private Version paidForceUpdateVersion;

    @SerializedName("paidNormalUpdateVersion")
    private Version paidNormalUpdateVersion;

    @SerializedName("feedback_text")
    private String feedbackText;

    @SerializedName("feedback_paid_text")
    private String feedbackProText;

    @SerializedName("download_paid_text")
    private String downloadPaidText;

    @SerializedName("ask_friend_text")
    private String askFriendText;

    @SerializedName("disclaimer_text")
    private String disclaimerText;

    @SerializedName("forceUpdateVersion")
    public Version getForceUpdateVersion() {
        return forceUpdateVersion;
    }

    @SerializedName("forceUpdateVersion")
    public void setForceUpdateVersion(Version forceUpdateVersion) {
        this.forceUpdateVersion = forceUpdateVersion;
    }

    @SerializedName("normalUpdateVersion")
    public Version getNormalUpdateVersion() {
        return normalUpdateVersion;
    }

    @SerializedName("normalUpdateVersion")
    public void setNormalUpdateVersion(Version normalUpdateVersion) {
        this.normalUpdateVersion = normalUpdateVersion;
    }

    @SerializedName("paidForceUpdateVersion")
    public Version getPaidForceUpdateVersion() {
        return paidForceUpdateVersion;
    }

    @SerializedName("paidForceUpdateVersion")
    public void setPaidForceUpdateVersion(Version paidForceUpdateVersion) {
        this.paidForceUpdateVersion = paidForceUpdateVersion;
    }

    @SerializedName("paidNormalUpdateVersion")
    public Version getPaidNormalUpdateVersion() {
        return paidNormalUpdateVersion;
    }

    @SerializedName("paidNormalUpdateVersion")
    public void setPaidNormalUpdateVersion(Version paidNormalUpdateVersion) {
        this.paidNormalUpdateVersion = paidNormalUpdateVersion;
    }

    @SerializedName("feedback_text")
    public String getFeedbackText() {
        return feedbackText;
    }

    @SerializedName("feedback_text")
    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }


    @SerializedName("feedback_paid_text")
    public String getFeedbackProText() {
        return feedbackProText;
    }

    @SerializedName("feedback_paid_text")
    public void setFeedbackProText(String feedbackProText) {
        this.feedbackProText = feedbackProText;
    }

    @SerializedName("download_paid_text")
    public String getDownloadPaidText() {
        return downloadPaidText;
    }

    @SerializedName("download_paid_text")
    public void setDownloadPaidText(String downloadPaidText) {
        this.downloadPaidText = downloadPaidText;
    }

    @SerializedName("ask_friend_text")
    public String getAskFriendText() {
        return askFriendText;
    }

    @SerializedName("ask_friend_text")
    public void setAskFriendText(String askFriendText) {
        this.askFriendText = askFriendText;
    }

    @SerializedName("disclaimer_text")
    public String getDisclaimerText() {
        return disclaimerText;
    }

    @SerializedName("disclaimer_text")
    public void setDisclaimerText(String disclaimerText) {
        this.disclaimerText = disclaimerText;
    }
}