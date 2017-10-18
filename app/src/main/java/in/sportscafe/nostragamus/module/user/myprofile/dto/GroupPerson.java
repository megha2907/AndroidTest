package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 12/6/16.
 */
@Parcel
public class GroupPerson {

    @SerializedName("user_id")
    private Integer id;

    @SerializedName("user_nick")
    private String userName;

    @SerializedName("user_photo")
    private String photo;

    @SerializedName("user_active")
    private boolean active;

    @SerializedName("is_admin")
    private boolean admin;

    @SerializedName("is_approved")
    private boolean approved;

    @SerializedName("user_id")
    public Integer getId() {
        return id;
    }

    @SerializedName("user_id")
    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("user_nick")
    public String getUserName() {
        return userName;
    }

    @SerializedName("user_nick")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("user_photo")
    public String getPhoto() {
        return photo;
    }

    @SerializedName("user_photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @SerializedName("user_active")
    public boolean isActive() {
        return active;
    }

    @SerializedName("user_active")
    public void setActive(boolean active) {
        this.active = active;
    }

    @SerializedName("is_admin")
    public boolean isAdmin() {
        return admin;
    }

    @SerializedName("is_admin")
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @SerializedName("is_approved")
    public boolean isApproved() {
        return approved;
    }

    @SerializedName("is_approved")
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        GroupPerson groupPerson = (GroupPerson) o;
        return getId() == groupPerson.getId();
    }
}