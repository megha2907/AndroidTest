package in.sportscafe.nostragamus.module.popups;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/2/17.
 */

@Parcel
public class PopUp {

    @SerializedName("id")
    private String popupId;

    @SerializedName("title")
    private String title;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("popup_name")
    private String name;

    @SerializedName("heading")
    private String heading;

    @SerializedName("description")
    private String description;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("alternate_design")
    private Boolean alternateDesign;

    @SerializedName("id")
    public String getPopupId() {
        return popupId;
    }
    @SerializedName("id")
    public void setPopupId(String popupId) {
        this.popupId = popupId;
    }

    @SerializedName("title")
    public String getTitle() {
        return title;
    }

    @SerializedName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @SerializedName("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @SerializedName("popup_name")
    public String getName() {
        return name;
    }

    @SerializedName("popup_name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("description")
    public String getDescription() {
        return description;
    }

    @SerializedName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("screen_name")
    public String getScreenName() {
        return screenName;
    }

    @SerializedName("screen_name")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @SerializedName("heading")
    public String getHeading() {
        return heading;
    }

    @SerializedName("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @SerializedName("alternate_design")
    public Boolean getAlternateDesign() {
        return alternateDesign;
    }

    @SerializedName("alternate_design")
    public void setAlternateDesign(Boolean alternateDesign) {
        this.alternateDesign = alternateDesign;
    }

}
