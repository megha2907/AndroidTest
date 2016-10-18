package in.sportscafe.scgame.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.module.user.badges.Badge;

/**
 * Created by Jeeva on 10/6/16.
 */
public class UserInfo {

    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("user_email")
    private String email;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_photo")
    private String photo;

    @JsonProperty("user_fb_id")
    private String fbId;

    @JsonProperty("user_google_id")
    private String googleId = "0";

    @JsonProperty("user_active")
    private boolean active = true;

    @JsonProperty("cookie")
    private String cookie;

    @JsonProperty("user_points")
    private Long points;

    @JsonProperty("user_nick")
    private String userNickName;

//    @JsonProperty("user_info")
//    private String userPowerupInfo;

    @JsonProperty("user_info")
    private PowerUpInfo powerUpInfo;

    /**
     * @return The id
     */
    @JsonProperty("user_id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The user_id
     */
    @JsonProperty("user_id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The email
     */
    @JsonProperty("user_email")
    public String getEmail() {
        return email;
    }

    /**
     * @param email The user_email
     */
    @JsonProperty("user_email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The userName
     */
    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The user_firstname
     */
    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The photo
     */
    @JsonProperty("user_photo")
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo The user_photo
     */
    @JsonIgnore
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return The fbId
     */
    @JsonProperty("user_fb_id")
    public String getFbId() {
        return fbId;
    }

    /**
     * @param fbId The user_fb_id
     */
    @JsonProperty("user_fb_id")
    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    /**
     * @return The googleId
     */
    @JsonProperty("user_google_id")
    public String getGoogleId() {
        return googleId;
    }

    /**
     * @param googleId The user_google_id
     */
    @JsonProperty("user_google_id")
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    /**
     * @return The active
     */
    @JsonProperty("user_active")
    public boolean getActive() {
        return active;
    }

    /**
     * @param active The user_active
     */
    @JsonProperty("user_active")
    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonProperty("cookie")
    public String getCookie() {
        return cookie;
    }

    @JsonProperty("cookie")
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @JsonProperty("user_points")
    public Long getPoints() {
        return points;
    }

    @JsonProperty("user_points")
    public void setPoints(Long points) {
        this.points = points;
    }

    @JsonProperty("user_info")
    public PowerUpInfo getUserInfo() {
        return powerUpInfo;
    }

    @JsonProperty("user_info")
    public void setUserInfo(PowerUpInfo powerUpInfo) {
        this.powerUpInfo = powerUpInfo;
    }

    @JsonProperty("user_nick")
    public String getUserNickName() {
        return userNickName;
    }

    @JsonProperty("user_nick")
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @JsonIgnore
    public HashMap<String, Integer> getPowerUps() {
        return powerUpInfo.getPowerUps();

    }

    @JsonIgnore
    public List<String> getBadges()
    {
        return powerUpInfo.getBadges();
    }

}