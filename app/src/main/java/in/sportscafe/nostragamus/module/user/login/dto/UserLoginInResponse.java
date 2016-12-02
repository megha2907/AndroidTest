package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 11/26/16.
 */

public class UserLoginInResponse extends JwtToken {


    @JsonProperty("new_user")
    private boolean newUser;

    @JsonProperty("user")
    private UserInfo userInfo;

    @JsonProperty("new_user")
    public boolean isNewUser() {
        return newUser;
    }
    @JsonProperty("new_user")
    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    @JsonProperty("user")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @JsonProperty("user")
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}
