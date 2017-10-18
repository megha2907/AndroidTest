package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 4/7/16.
 */
public class LogInRequest {

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("accessExpiry")
    private Long accessExpiry = 123456789L;

//    @SerializedName("access_provider")
//    private String accessPovider;

    @SerializedName("profile")
    private UserProfile userProfile;

    @SerializedName("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    @SerializedName("accessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @SerializedName("accessExpiry")
    public Long getAccessExpiry() {
        return accessExpiry;
    }

    @SerializedName("accessExpiry")
    public void setAccessExpiry(Long accessExpiry) {
        this.accessExpiry = accessExpiry;
    }

//    @SerializedName("access_provider")
//    public String getAccessPovider() {
//        return accessPovider;
//    }
//
//    @SerializedName("access_provider")
//    public void setAccessPovider(String accessProvider) {
//        this.accessPovider = accessProvider;
//    }

    @SerializedName("profile")
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @SerializedName("profile")
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @SerializedName("refreshToken")
    public String getRefreshToken() {
        return refreshToken;
    }

    @SerializedName("refreshToken")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}