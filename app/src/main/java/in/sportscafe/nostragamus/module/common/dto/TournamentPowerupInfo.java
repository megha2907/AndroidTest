package in.sportscafe.nostragamus.module.common.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by deepanshi on 2/1/17.
 */
@Parcel
public class TournamentPowerupInfo {

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @SerializedName("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }
}
