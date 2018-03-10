package in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/22/18.
 */
@Parcel
public class HowToPlay {

    @SerializedName("description")
    private String desc;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("heading")
    private String heading;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

}
