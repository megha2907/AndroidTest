package in.sportscafe.nostragamus.module.user.badges;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 25/8/16.
 */
public class Badge {

    @JsonProperty("name")
    private String name;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("img_url")
    private String photo;

    public Badge() {
    }

    public Badge(String name) {
        this.name = name;
        this.desc = desc;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("desc")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JsonProperty("img_url")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("img_url")
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
