package in.sportscafe.nostragamus.module.onboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 21/10/16.
 */

@Parcel
public class OnBoardingDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("imageResName")
    private String imageResName;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("referralCode")
    private String referralCode;

    @JsonIgnore
    private Boolean isReferral=false;

    public OnBoardingDto() {
    }

    public OnBoardingDto(String title, String desc, String imageResName) {
        this.title = title;
        this.desc = desc;
        this.imageResName = imageResName;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JsonProperty("imageResName")
    public String getImageResName() {
        return imageResName;
    }

    @JsonProperty("imageResName")
    public void setImageResName(String imageResName) {
        this.imageResName = imageResName;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonIgnore
    public Boolean getReferral() {
        return isReferral;
    }

    @JsonIgnore
    public void setReferral(Boolean referral) {
        isReferral = referral;
    }

    @JsonProperty("referralCode")
    public String getReferralCode() {
        return referralCode;
    }

    @JsonProperty("referralCode")
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }


}