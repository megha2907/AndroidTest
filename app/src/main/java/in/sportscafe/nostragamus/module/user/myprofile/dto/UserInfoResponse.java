package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 30/6/16.
 */
public class UserInfoResponse {

    @SerializedName("data")
    private UserInfo userInfo;

    @SerializedName("data")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @SerializedName("data")
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}