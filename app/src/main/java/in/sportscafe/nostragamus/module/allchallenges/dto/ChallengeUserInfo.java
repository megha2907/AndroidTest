package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by deepanshi on 2/21/17.
 */
@Parcel
public class ChallengeUserInfo {

    @JsonProperty("paid_amount")
    private Integer paidAmount;

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

    @JsonProperty("paid_amount")
    public Integer getPaidAmount() {
        return paidAmount;
    }

    @JsonProperty("paid_amount")
    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

}
