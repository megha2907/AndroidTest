package in.sportscafe.nostragamus.module.upgradeToPro.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 04/08/17.
 */

public class UpgradeToProResponse {

    @SerializedName("enabled")
    private boolean shouldShowUpgradeDialog;

    @SerializedName("contentImageUrl")
    private String contentImageUrl;

    @SerializedName("appLink")
    private String proAppDownloadLink;

    public boolean isShouldShowUpgradeDialog() {
        return shouldShowUpgradeDialog;
    }

    public void setShouldShowUpgradeDialog(boolean shouldShowUpgradeDialog) {
        this.shouldShowUpgradeDialog = shouldShowUpgradeDialog;
    }

    public String getContentImageUrl() {
        return contentImageUrl;
    }

    public void setContentImageUrl(String contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }

    public String getProAppDownloadLink() {
        return proAppDownloadLink;
    }

    public void setProAppDownloadLink(String proAppDownloadLink) {
        this.proAppDownloadLink = proAppDownloadLink;
    }
}
