package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 21/07/17.
 */

public class AlreadyTransferredPowerupDto implements Cloneable {

    @SerializedName("2x")
    private int doubler;

    @SerializedName("no_negs")
    private int noNegative;

    @SerializedName("player_poll")
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
