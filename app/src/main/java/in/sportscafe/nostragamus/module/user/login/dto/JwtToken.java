package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 12/2/16.
 */

public class JwtToken {

    @SerializedName("jwt_token")
    private String token;

    @SerializedName("expiry")
    private Long expiry;

    @SerializedName("jwt_token")
    public String getToken() {
        return token;
    }

    @SerializedName("jwt_token")
    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("expiry")
    public Long getExpiry() {
        return expiry;
    }

    @SerializedName("expiry")
    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }
}
