package in.sportscafe.scgame.module.user.myprofile.edit;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 13/7/16.
 */
public class UpdateUserRequest {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_photo")
    private String userPhoto;

    @JsonProperty("user_nick")
    private String userNickName;

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

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