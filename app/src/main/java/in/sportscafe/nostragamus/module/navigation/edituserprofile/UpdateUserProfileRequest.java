package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 8/22/17.
 */

public class UpdateUserProfileRequest {

    @SerializedName("user_photo")
    private String userPhoto;

    @SerializedName("user_nick")
    private String userNickName;

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


}
