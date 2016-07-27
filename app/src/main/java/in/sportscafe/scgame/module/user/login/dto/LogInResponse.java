package in.sportscafe.scgame.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogInResponse {

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