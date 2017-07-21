package in.sportscafe.nostragamus.module.play.powerup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 21/07/17.
 */

public class AlreadyTransferredPowerupDto implements Cloneable {

    @JsonProperty("2x")
    private int doubler;

    @JsonProperty("no_negs")
    private int noNegative;

    @JsonProperty("player_poll")
    private int audiencePoll;

    public AlreadyTransferredPowerupDto clone() {
        try {
            return (AlreadyTransferredPowerupDto)super.clone();
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
}
