package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 8/22/17.
 */

public class UpdateUserProfileRequest {

    @JsonProperty("user_photo")
    private String userPhoto;

    @JsonProperty("user_nick")
    private String userNickName;

    @JsonProperty("user_photo")
    public String getUserPhoto() {
        return userPhoto;
    }

    @JsonProperty("user_photo")
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }


    @JsonProperty("user_nick")
    public String getUserNickName() {
        return userNickName;
    }

    @JsonProperty("user_nick")
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }


}
