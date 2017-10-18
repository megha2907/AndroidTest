package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 18/09/17.
 */
@Parcel
public class PowerUp {

    @SerializedName("2x")
    private int doubler = 0;

    @SerializedName("no_negs")
    private int noNegative = 0;

    @SerializedName("player_poll")
    private int playerPoll = 0;

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

    public int getPlayerPoll() {
        return playerPoll;
    }

    public void setPlayerPoll(int playerPoll) {
        this.playerPoll = playerPoll;
    }
}
