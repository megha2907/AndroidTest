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

    @SerializedName("banner_id")
    private int bannerId;

    @SerializedName("banner_name")
    private String bannerName;

    @SerializedName("url")
    private String bannerImageUrl;

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

    public NostraNotification getNostraBannerInfo() {
        return nostraBannerInfo;
    }

    public void setNostraBannerInfo(NostraNotification nostraBannerInfo) {
        this.nostraBannerInfo = nostraBannerInfo;
    }

}
