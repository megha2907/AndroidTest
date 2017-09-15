package in.sportscafe.nostragamus.module.navigation.appupdate;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 6/2/17.
 */

@Parcel
public class AppUpdateDto {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String desc;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("heading")
    private String heading;

    public AppUpdateDto() {
    }

    public AppUpdateDto(String title, String desc, String imageUrl) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    @SerializedName("title")
    public String getTitle() {
        return title;
    }

    @SerializedName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("description")
    public String getDesc() {
        return desc;
    }

    @SerializedName("description")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @SerializedName("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @SerializedName("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @SerializedName("heading")
    public String getHeading() {
        return heading;
    }

    @SerializedName("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }
}