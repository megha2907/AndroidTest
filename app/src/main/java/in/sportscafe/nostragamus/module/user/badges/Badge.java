package in.sportscafe.nostragamus.module.user.badges;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 25/8/16.
 */
@Parcel
public class Badge {

    @SerializedName("name")
    private String name;

    @SerializedName("desc")
    private String desc;

    @SerializedName("img_url")
    private String photo;

    @SerializedName("is_locked")
    private Boolean isLocked;

    public Badge() {
    }

    public Badge(String name) {
        this.name = name;
        this.desc = desc;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("desc")
    public String getDesc() {
        return desc;
    }

    @SerializedName("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @SerializedName("img_url")
    public String getPhoto() {
        return photo;
    }

    @SerializedName("img_url")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @SerializedName("is_locked")
    public Boolean getIsLocked() {
        return isLocked;
    }

    @SerializedName("is_locked")
    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }
}
