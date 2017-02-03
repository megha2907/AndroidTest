package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by deepanshi on 11/26/16.
 */

public class UserProfile {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_referral_id")
    private String userReferralId;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("profileUrl")
    private String profileUrl;
//
//    @JsonProperty("emails")
//    private Emails emails;
//
//    @JsonProperty("photos")
//    private Photos photos;


    @JsonProperty("emails")
    private String emails;

    @JsonProperty("photos")
    private String photos;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("username")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("username")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("profileUrl")
    public String getProfileUrl() {
        return profileUrl;
    }

    @JsonProperty("profileUrl")
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

//    @JsonProperty("emails")
//    public Emails getEmails() {
//        return emails;
//    }
//
//    @JsonProperty("emails")
//    public void setEmails(Emails emails) {
//        this.emails = emails;
//    }

//    @JsonProperty("photos")
//    public Photos getPhotos() {
//        return photos;
//    }
//
//    @JsonProperty("photos")
//    public void setPhotos(Photos photos) {
//        this.photos = photos;
//    }

    @JsonProperty("provider")
    public String getProvider() {
        return provider;
    }
    @JsonProperty("provider")
    public void setProvider(String provider) {
        this.provider = provider;
    }

    @JsonProperty("emails")
    public String getEmails() {
        return emails;
    }

    @JsonProperty("emails")
    public void setEmails(String emails) {
        this.emails = emails;
    }

    @JsonProperty("photos")
    public String getPhotos() {
        return photos;
    }
    @JsonProperty("photos")
    public void setPhotos(String photos) {
        this.photos = photos;
    }

    @JsonProperty("user_referral_id")
    public String getUserReferralId() {
        return userReferralId;
    }

    @JsonProperty("user_referral_id")
    public void setUserReferralId(String userReferralId) {
        if(null == userReferralId) {
            userReferralId = "";
        }
        this.userReferralId = userReferralId;
    }

}
