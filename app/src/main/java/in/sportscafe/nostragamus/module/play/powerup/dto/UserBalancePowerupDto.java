package in.sportscafe.nostragamus.module.play.powerup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by sandip on 21/07/17.
 */

public class UserBalancePowerupDto implements Cloneable {

    @JsonProperty("2x")
    private int doubler;

    @JsonProperty("no_negs")
    private int noNegative;

    @JsonProperty("player_poll")
    private int audiencePoll;

    public UserBalancePowerupDto() {
        doubler = 0;
        noNegative = 0;
        audiencePoll = 0;
    }

    public UserBalancePowerupDto clone() {
        try {
            return (UserBalancePowerupDto)super.clone();
        }
        catch (CloneNotSupportedException e) {}
        return null;
    }

    public int getDoubler() {
        return doubler;
    }

    public void setDoubler(int doubler) {
        this.doubler = doubler;
    }

    public int getNoNegative() {
        return noNegative;
    }

    public void setNoNegative(int noNegative) {
        this.noNegative = noNegative;
    }

    public int getAudiencePoll() {
        return audiencePoll;
    }

    public void setAudiencePoll(int audiencePoll) {
        this.audiencePoll = audiencePoll;
    }

    public HashMap<String, Integer> getValueMap() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put(Constants.Powerups.XX, getDoubler());
        map.put(Constants.Powerups.NO_NEGATIVE, getNoNegative());
        map.put(Constants.Powerups.AUDIENCE_POLL, getAudiencePoll());

        return map;
    }

}
