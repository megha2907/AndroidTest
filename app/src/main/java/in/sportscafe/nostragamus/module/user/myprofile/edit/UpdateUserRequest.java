package in.sportscafe.nostragamus.module.user.myprofile.edit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 13/7/16.
 */
public class UpdateUserRequest {

    @SerializedName("user_photo")
    private String userPhoto;

    @SerializedName("user_nick")
    private String userNickName;

    @SerializedName("user_accepted")
    private boolean disclaimerAccepted;

    @SerializedName("user_referral_code")
    private String referralCode;

    @SerializedName("user_photo")
    public String getUserPhoto() {
        return userPhoto;
    }

    @SerializedName("user_photo")
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }


    @SerializedName("user_nick")
    public String getUserNickName() {
        return userNickName;
    }

    @SerializedName("user_nick")
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @SerializedName("user_accepted")
    public boolean isDisclaimerAccepted() {
        return disclaimerAccepted;
    }

    @SerializedName("user_accepted")
    public void setDisclaimerAccepted(boolean disclaimerAccepted) {
        this.disclaimerAccepted = disclaimerAccepted;
    }

    @SerializedName("user_referral_code")
    public String getReferralCode() {
        return referralCode;
    }

    @SerializedName("user_referral_code")
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

}