package in.sportscafe.scgame.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.scgame.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 30/6/16.
 */
public class UserInfoResponse {

    @JsonProperty("data")
    private UserInfo userInfo;

    @JsonProperty("data")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @JsonProperty("data")
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}