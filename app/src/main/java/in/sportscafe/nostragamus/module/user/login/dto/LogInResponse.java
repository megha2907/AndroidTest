package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogInResponse {

    @JsonProperty("data")
    private UserLoginInResponse userLoginInResponse;

    @JsonProperty("data")
    public UserLoginInResponse getUserLoginInResponse() {
        return userLoginInResponse;
    }

    @JsonProperty("data")
    public void setUserLoginInResponse(UserLoginInResponse userLoginInResponse) {
        this.userLoginInResponse = userLoginInResponse;
    }
}