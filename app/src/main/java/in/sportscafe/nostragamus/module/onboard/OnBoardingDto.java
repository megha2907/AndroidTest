package in.sportscafe.nostragamus.module.onboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 21/10/16.
 */

@Parcel
public class OnBoardingDto {

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String desc;

    @SerializedName("imageResName")
    private String imageResName;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("referralCode")
    private String referralCode;


    private Boolean isReferral=false;

    public OnBoardingDto() {
    }

    public OnBoardingDto(String title, String desc, String imageResName) {
        this.title = title;
        this.desc = desc;
        this.imageResName = imageResName;
    }

    @SerializedName("title")
    public String getTitle() {
        return title;
    }

    @SerializedName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("desc")
    public String getDesc() {
        return desc;
    }

    @SerializedName("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @SerializedName("imageResName")
    public String getImageResName() {
        return imageResName;
    }

    @SerializedName("imageResName")
    public void setImageResName(String imageResName) {
        this.imageResName = imageResName;
    }

    @SerializedName("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @SerializedName("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Boolean getReferral() {
        return isReferral;
    }


    public void setReferral(Boolean referral) {
        isReferral = referral;
    }

    @SerializedName("referralCode")
    public String getReferralCode() {
        return referralCode;
    }

    @SerializedName("referralCode")
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }


}