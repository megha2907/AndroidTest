package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by deepanshi on 11/26/16.
 */

public class UserProfile {

    @SerializedName("id")
    private String id;

    @SerializedName("user_referral_code")
    private String userReferralCode;

    @SerializedName("username")
    private String userName;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("profileUrl")
    private String profileUrl;

    @SerializedName("campaignName")
    private String campaignName;

    @SerializedName("appType")
    private String appType;

    @SerializedName("wallet_init")
    private Integer walletInitialAmount;

//
//    @SerializedName("emails")
//    private Emails emails;
//
//    @SerializedName("photos")
//    private Photos photos;


    @SerializedName("emails")
    private String emails;

    @SerializedName("photos")
    private String photos;

    @SerializedName("provider")
    private String provider;

    @SerializedName("id")
    public String getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("username")
    public String getUserName() {
        return userName;
    }

    @SerializedName("username")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @SerializedName("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @SerializedName("gender")
    public String getGender() {
        return gender;
    }

    @SerializedName("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("profileUrl")
    public String getProfileUrl() {
        return profileUrl;
    }

    @SerializedName("profileUrl")
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

//    @SerializedName("emails")
//    public Emails getEmails() {
//        return emails;
//    }
//
//    @SerializedName("emails")
//    public void setEmails(Emails emails) {
//        this.emails = emails;
//    }

//    @SerializedName("photos")
//    public Photos getPhotos() {
//        return photos;
//    }
//
//    @SerializedName("photos")
//    public void setPhotos(Photos photos) {
//        this.photos = photos;
//    }

    @SerializedName("provider")
    public String getProvider() {
        return provider;
    }
    @SerializedName("provider")
    public void setProvider(String provider) {
        this.provider = provider;
    }

    @SerializedName("emails")
    public String getEmails() {
        return emails;
    }

    @SerializedName("emails")
    public void setEmails(String emails) {
        this.emails = emails;
    }

    @SerializedName("photos")
    public String getPhotos() {
        return photos;
    }
    @SerializedName("photos")
    public void setPhotos(String photos) {
        this.photos = photos;
    }

    @SerializedName("user_referral_code")
    public String getUserReferralCode() {
        return userReferralCode;
    }

    @SerializedName("user_referral_code")
    public void setUserReferralCode(String userReferralCode) {
        if(null == userReferralCode) {
            userReferralCode = "";
        }
        this.userReferralCode = userReferralCode;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    @SerializedName("wallet_init")
    public Integer getWalletInitialAmount() {
        return walletInitialAmount;
    }

    @SerializedName("wallet_init")
    public void setWalletInitialAmount(Integer walletInitialAmount) {
        this.walletInitialAmount = walletInitialAmount;
    }

    @SerializedName("appType")
    public String getAppType() {
        return appType;
    }

    @SerializedName("appType")
    public void setAppType(String appType) {
        this.appType = appType;
    }

}
