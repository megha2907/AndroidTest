package in.sportscafe.nostragamus.module.user.sportselection.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.ImageConfig;

/**
 * Created by Jeeva on 27/5/16.
 */
@Parcel
public class Sport {

    @JsonProperty("sports_id")
    private int id;

    private int state;

    @JsonProperty("sports_name")
    private String name;

    public Sport() {
    }

    public Sport(int id) {
        this.id = id;
    }

    @JsonProperty("sports_id")
    public int getId() {
        return id;
    }

    @JsonProperty("sports_id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public String getImageUrl() {
        return getSportImageUrl(name, 100, 100);
    }

    @JsonIgnore
    public String getSelectedImageUrl() {
        return getSportImageUrl(name, 100, 100);
    }

    @JsonIgnore
    public int getState() {
        return state;
    }

    @JsonIgnore
    public void setState(int state) {
        this.state = state;
    }

    @JsonProperty("sports_name")
    public String getName() {
        return name;
    }

    @JsonProperty("sports_name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        Sport sport = (Sport) o;
        return id == sport.getId();
    }

    @Override
    public String toString() {
        return name;
    }

    public static String getSportImageUrl(String sportName, int width, int height) {
        return ImageConfig.getSportImageUrl(width, height, sportName.replace(" ", "").toLowerCase() + "w");
    }
}