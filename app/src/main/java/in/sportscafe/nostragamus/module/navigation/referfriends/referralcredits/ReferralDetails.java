package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 6/27/17.
 */

public class ReferralDetails {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_name")
    private Integer userName;

    @JsonProperty("user_photo")
    private Integer userPhoto;

    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("user_name")
    public Integer getUserName() {
        return userName;
    }

    @JsonProperty("user_name")
    public void setUserName(Integer userName) {
        this.userName = userName;
    }

    @JsonProperty("user_photo")
    public Integer getUserPhoto() {
        return userPhoto;
    }

    @JsonProperty("user_photo")
    public void setUserPhoto(Integer userPhoto) {
        this.userPhoto = userPhoto;
    }
}
