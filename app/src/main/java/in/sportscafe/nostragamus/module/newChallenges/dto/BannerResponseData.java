package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.notifications.NostraNotificationData;

/**
 * Created by deepanshi on 2/1/18.
 */

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
    private BannerInfo bannerInfo;

    @SerializedName("screenName")
    private String screenName;

    @SerializedName("screenData")
    private NostraNotificationData data;

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

    public BannerInfo getBannerInfo() {
        return bannerInfo;
    }

    public void setBannerInfo(BannerInfo bannerInfo) {
        this.bannerInfo = bannerInfo;
    }

    public String getScreenName() {
        return screenName;
    }
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public NostraNotificationData getData() {
        return data;
    }

    public void setData(NostraNotificationData data) {
        this.data = data;
    }


}
