package in.sportscafe.nostragamus.module.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by deepanshi on 2/1/17.
 */
@Parcel
public class TournamentPowerupInfo {

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }
}
