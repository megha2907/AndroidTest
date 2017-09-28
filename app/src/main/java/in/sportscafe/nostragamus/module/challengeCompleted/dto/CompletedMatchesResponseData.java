package in.sportscafe.nostragamus.module.challengeCompleted.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedMatchesResponseData {

    @SerializedName("matches")
    private List<CompletedMatch> completedMatchList;

    @SerializedName("powerups")
    private PowerUp powerUp;

    public List<CompletedMatch> getCompletedMatchList() {
        return completedMatchList;
    }

    public void setCompletedMatchList(List<CompletedMatch> completedMatchList) {
        this.completedMatchList = completedMatchList;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }
}
