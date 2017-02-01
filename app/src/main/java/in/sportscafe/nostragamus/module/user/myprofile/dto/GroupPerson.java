package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 12/6/16.
 */
@Parcel
public class GroupPerson {

    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("user_nick")
    private String userName;

    @JsonProperty("user_photo")
    private String photo;

    @JsonProperty("user_active")
    private boolean active;

    @JsonProperty("is_admin")
    private boolean admin;

    @JsonProperty("is_approved")
    private boolean approved;

    @JsonProperty("user_id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("user_id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("user_nick")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("user_nick")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("user_photo")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("user_photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @JsonProperty("user_active")
    public boolean isActive() {
        return active;
    }

    @JsonProperty("user_active")
    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonProperty("is_admin")
    public boolean isAdmin() {
        return admin;
    }

    @JsonProperty("is_admin")
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @JsonProperty("is_approved")
    public boolean isApproved() {
        return approved;
    }

    @JsonProperty("is_approved")
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        GroupPerson groupPerson = (GroupPerson) o;
        return getId() == groupPerson.getId();
    }
}