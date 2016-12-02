package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 12/2/16.
 */

public class JwtToken {

    @JsonProperty("jwt_token")
    private String token;

    @JsonProperty("expiry")
    private Long expiry;

    @JsonProperty("jwt_token")
    public String getToken() {
        return token;
    }

    @JsonProperty("jwt_token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("expiry")
    public Long getExpiry() {
        return expiry;
    }

    @JsonProperty("expiry")
    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }
}
