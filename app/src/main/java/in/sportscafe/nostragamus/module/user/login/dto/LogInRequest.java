package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 4/7/16.
 */
public class LogInRequest {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_expiry")
    private Long accessExpiry = 123456789L;

    @JsonProperty("access_provider")
    private String accessPovider;

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("access_expiry")
    public Long getAccessExpiry() {
        return accessExpiry;
    }

    @JsonProperty("access_expiry")
    public void setAccessExpiry(Long accessExpiry) {
        this.accessExpiry = accessExpiry;
    }

    @JsonProperty("access_provider")
    public String getAccessPovider() {
        return accessPovider;
    }

    @JsonProperty("access_provider")
    public void setAccessPovider(String accessProvider) {
        this.accessPovider = accessProvider;
    }
}