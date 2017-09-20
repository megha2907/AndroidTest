package in.sportscafe.nostragamus.module.prediction.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 20/09/17.
 */

public class AnswerPowerUpDto {

    @SerializedName("2x")
    private boolean doubler = false;

    @SerializedName("no_negs")
    private boolean noNegative = false;

    @SerializedName("player_poll")
    private boolean playerPoll = false;

    public boolean isDoubler() {
        return doubler;
    }

    public void setDoubler(boolean doubler) {
        this.doubler = doubler;
    }

    public boolean isNoNegative() {
        return noNegative;
    }

    public void setNoNegative(boolean noNegative) {
        this.noNegative = noNegative;
    }

    public boolean isPlayerPoll() {
        return playerPoll;
    }

    public void setPlayerPoll(boolean playerPoll) {
        this.playerPoll = playerPoll;
    }
}
