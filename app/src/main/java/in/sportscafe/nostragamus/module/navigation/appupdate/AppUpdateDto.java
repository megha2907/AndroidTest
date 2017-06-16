package in.sportscafe.nostragamus.module.navigation.appupdate;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 6/2/17.
 */

@Parcel
public class AppUpdateDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String desc;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("heading")
    private String heading;

    public AppUpdateDto() {
    }

    public AppUpdateDto(String title, String desc, String imageUrl) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("description")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("heading")
    public String getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }
}