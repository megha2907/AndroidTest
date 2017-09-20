package in.sportscafe.nostragamus.module.prediction.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 18/09/17.
 */
@Parcel
public class PowerUp {

    private int doubler = 0;

    private int noNegative = 0;

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
