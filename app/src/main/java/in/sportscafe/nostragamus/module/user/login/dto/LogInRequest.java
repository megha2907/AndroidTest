package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 4/7/16.
 */
public class LogInRequest {

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("accessExpiry")
    private Long accessExpiry = 123456789L;

//    @JsonProperty("access_provider")
//    private String accessPovider;

    @JsonProperty("profile")
    private UserProfile userProfile;

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("accessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("accessExpiry")
    public Long getAccessExpiry() {
        return accessExpiry;
    }

    @JsonProperty("accessExpiry")
    public void setAccessExpiry(Long accessExpiry) {
        this.accessExpiry = accessExpiry;
    }

//    @JsonProperty("access_provider")
//    public String getAccessPovider() {
//        return accessPovider;
//    }
//
//    @JsonProperty("access_provider")
//    public void setAccessPovider(String accessProvider) {
//        this.accessPovider = accessProvider;
//    }

    @JsonProperty("profile")
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @JsonProperty("profile")
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @JsonProperty("refreshToken")
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("refreshToken")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}