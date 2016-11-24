package com.jeeva.android.facebook.user;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nandakumar on 18/4/15.
 */

public class FacebookProfile implements Serializable {

    private String email;

    private String id;

    private String accessToken;

    private Date tokenExpiry;

    private String first_name;

    private String last_name;

    private String gender;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(Date tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePictureUrl(int width, int height) {
        return "https://graph.facebook.com/" + id + "/picture?height=" + height + "&width=" + width;
    }
}