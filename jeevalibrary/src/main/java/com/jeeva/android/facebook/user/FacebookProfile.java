package com.jeeva.android.facebook.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nandakumar on 18/4/15.
 */

public class FacebookProfile implements Serializable {

    @JsonProperty("email")
    private String email;

    @JsonProperty("id")
    private String id;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private Date tokenExpiry;

    @JsonProperty("first_name")
    private String first_name;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getAccessToken() {
        return accessToken;
    }

    @JsonIgnore
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonIgnore
    public Date getTokenExpiry() {
        return tokenExpiry;
    }

    @JsonIgnore
    public void setTokenExpiry(Date tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    @JsonProperty("first_name")
    public String getFirst_name() {
        return first_name;
    }

    @JsonProperty("first_name")
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @JsonProperty("last_name")
    public String getLast_name() {
        return last_name;
    }

    @JsonProperty("last_name")
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonIgnore
    public String getProfilePictureUrl(int width, int height) {
        return "https://graph.facebook.com/" + id + "/picture?height=" + height + "&width=" + width;
    }
}