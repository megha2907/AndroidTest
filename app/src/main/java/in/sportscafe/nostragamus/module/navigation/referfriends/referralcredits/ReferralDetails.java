package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 6/27/17.
 */

@Parcel
public class ReferralDetails {

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("user_photo")
    private String userPhoto;

    @SerializedName("user_id")
    public Integer getUserId() {
        return userId;
    }

    @SerializedName("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @SerializedName("user_name")
    public String getUserName() {
        return userName;
    }

    @SerializedName("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("user_photo")
    public String getUserPhoto() {
        return userPhoto;
    }

    @SerializedName("user_photo")
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
