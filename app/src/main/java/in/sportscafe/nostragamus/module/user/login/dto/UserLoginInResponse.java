package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 11/26/16.
 */

public class UserLoginInResponse extends JwtToken {


    @SerializedName("new_user")
    private boolean newUser;

    @SerializedName("user")
    private UserInfo userInfo;

    @SerializedName("new_user")
    public boolean isNewUser() {
        return newUser;
    }
    @SerializedName("new_user")
    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    @SerializedName("user")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @SerializedName("user")
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}
