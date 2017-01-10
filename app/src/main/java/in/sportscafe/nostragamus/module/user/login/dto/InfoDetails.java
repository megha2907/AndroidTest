package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by deepanshi on 10/8/16.
 */
public class InfoDetails {

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("badges")
    private List<String> badges = new ArrayList<>();

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @JsonProperty("badges")
    public List<String> getBadges() {
        return badges;
    }

    @JsonProperty("badges")
    public void setBadges(List<String> badges) {
        this.badges = badges;
    }

}