package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.notifications.NostraNotification;
import in.sportscafe.nostragamus.module.notifications.NostraNotificationData;

/**
 * Created by deepanshi on 2/1/18.
 */
@Parcel
public class BannerResponseData {

    @SerializedName("id")
    private int bannerId;

    @SerializedName("name")
    private String bannerName;

    @SerializedName("url")
    private String bannerImageUrl;

    @SerializedName("target_screen")
    private String targetScreen;

    @SerializedName("is_active")
    private boolean isActive;

    @SerializedName("priority")
    private int priority;

    @SerializedName("info")
    private NostraNotification nostraBannerInfo;

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getTargetScreen() {
        return targetScreen;
    }

    public void setTargetScreen(String targetScreen) {
        this.targetScreen = targetScreen;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public NostraNotification getNostraBannerInfo() {
        return nostraBannerInfo;
    }

    public void setNostraBannerInfo(NostraNotification nostraBannerInfo) {
        this.nostraBannerInfo = nostraBannerInfo;
    }


}
