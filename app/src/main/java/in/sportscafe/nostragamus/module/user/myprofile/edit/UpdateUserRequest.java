package in.sportscafe.nostragamus.module.user.myprofile.edit;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 13/7/16.
 */
public class UpdateUserRequest {


    @JsonProperty("user_photo")
    private String userPhoto;

    @JsonProperty("user_nick")
    private String userNickName;

    @JsonProperty("user_accepted")
    private boolean userAccepted;

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

    public boolean isUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(boolean userAccepted) {
        this.userAccepted = userAccepted;
    }
}