package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

public class LogInResponse {

    @SerializedName("data")
    private UserLoginInResponse userLoginInResponse;

    @SerializedName("data")
    public UserLoginInResponse getUserLoginInResponse() {
        return userLoginInResponse;
    }

    @SerializedName("data")
    public void setUserLoginInResponse(UserLoginInResponse userLoginInResponse) {
        this.userLoginInResponse = userLoginInResponse;
    }
}