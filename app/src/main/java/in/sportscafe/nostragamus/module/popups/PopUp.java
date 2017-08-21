package in.sportscafe.nostragamus.module.popups;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/2/17.
 */

@Parcel
public class PopUp {

    @JsonProperty("id")
    private String popupId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("popup_name")
    private String name;

    @JsonProperty("heading")
    private String heading;

    @JsonProperty("description")
    private String description;

    @JsonProperty("screen_name")
    private String screenName;

    @JsonProperty("alternate_design")
    private Boolean alternateDesign;

    @JsonProperty("id")
    public String getPopupId() {
        return popupId;
    }
    @JsonProperty("id")
    public void setPopupId(String popupId) {
        this.popupId = popupId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("popup_name")
    public String getName() {
        return name;
    }

    @JsonProperty("popup_name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("screen_name")
    public String getScreenName() {
        return screenName;
    }

    @JsonProperty("screen_name")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @JsonProperty("heading")
    public String getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @JsonProperty("alternate_design")
    public Boolean getAlternateDesign() {
        return alternateDesign;
    }

    @JsonProperty("alternate_design")
    public void setAlternateDesign(Boolean alternateDesign) {
        this.alternateDesign = alternateDesign;
    }

}
