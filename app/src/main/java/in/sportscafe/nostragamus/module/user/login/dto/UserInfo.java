package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by Jeeva on 10/6/16.
 */
@Parcel
public class UserInfo extends PlayerInfo {

    @SerializedName("user_email")
    private String email;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("user_fb_id")
    private String fbId;

    @SerializedName("user_google_id")
    private String googleId = "0";

    @SerializedName("user_active")
    private boolean active = true;

    @SerializedName("cookie")
    private String cookie;

    @SerializedName("count_groups")
    private Integer totalGroups;

    /**
     * @return The email
     */
    @SerializedName("user_email")
    public String getEmail() {
        return email;
    }

    /**
     * @param email The user_email
     */
    @SerializedName("user_email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The userName
     */
    @SerializedName("user_name")
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The user_firstname
     */
    @SerializedName("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The fbId
     */
    @SerializedName("user_fb_id")
    public String getFbId() {
        return fbId;
    }

    /**
     * @param fbId The user_fb_id
     */
    @SerializedName("user_fb_id")
    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    /**
     * @return The googleId
     */
    @SerializedName("user_google_id")
    public String getGoogleId() {
        return googleId;
    }

    /**
     * @param googleId The user_google_id
     */
    @SerializedName("user_google_id")
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    /**
     * @return The active
     */
    @SerializedName("user_active")
    public boolean getActive() {
        return active;
    }

    /**
     * @param active The user_active
     */
    @SerializedName("user_active")
    public void setActive(boolean active) {
        this.active = active;
    }

    @SerializedName("cookie")
    public String getCookie() {
        return cookie;
    }

    @SerializedName("cookie")
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @SerializedName("count_groups")
    public Integer getTotalGroups() {
        return totalGroups;
    }

    @SerializedName("count_groups")
    public void setTotalGroups(Integer totalGroups) {
        this.totalGroups = totalGroups;
    }


    public HashMap<String, Integer> getPowerUps() {
        return getInfoDetails().getPowerUps();
    }

}