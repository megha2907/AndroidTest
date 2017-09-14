package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/14/17.
 */
@Parcel
public class PowerUpInfo {

    @JsonProperty("2x")
    private Integer powerUp2x;

    @JsonProperty("no_negs")
    private Integer powerUpNoNeg;

    @JsonProperty("player_poll")
    private Integer powerUpPlayerPoll;

    public Integer getPowerUp2x() {
        return powerUp2x;
    }

    public void setPowerUp2x(Integer powerUp2x) {
        this.powerUp2x = powerUp2x;
    }

    public Integer getPowerUpNoNeg() {
        return powerUpNoNeg;
    }

    public void setPowerUpNoNeg(Integer powerUpNoNeg) {
        this.powerUpNoNeg = powerUpNoNeg;
    }

    public Integer getPowerUpPlayerPoll() {
        return powerUpPlayerPoll;
    }

    public void setPowerUpPlayerPoll(Integer powerUpPlayerPoll) {
        this.powerUpPlayerPoll = powerUpPlayerPoll;
    }
}
