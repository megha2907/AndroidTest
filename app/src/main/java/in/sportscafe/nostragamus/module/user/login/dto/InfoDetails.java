package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.module.user.badges.Badge;

/**
 * Created by deepanshi on 10/8/16.
 */
@Parcel
public class InfoDetails {

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("badges")
    private List<Badge> badges = new ArrayList<>();

    @JsonProperty("transient_badges")
    private List<Badge> transientBadges = new ArrayList<>();

    @JsonProperty("level")
    private String level;

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @JsonProperty("badges")
    public List<Badge> getBadges() {
        return badges;
    }

    @JsonProperty("badges")
    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(String level) {
        this.level = level;
    }

    @JsonProperty("transient_badges")
    public List<Badge> getTransientBadges() {
        return transientBadges;
    }

    @JsonProperty("transient_badges")
    public void setTransientBadges(List<Badge> transientBadges) {
        this.transientBadges = transientBadges;
    }

}