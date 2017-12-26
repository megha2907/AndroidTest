package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedMatch;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 23/09/17.
 */

public class InPlayMatchesResponseData {

    @SerializedName("matches")
    private List<InPlayMatch> inPlayMatchList;

    @SerializedName("powerups")
    private PowerUp powerUp;

    @SerializedName("timeline")
    private List<InPlayContestMatchDto> inPlayContestMatchDtoList;

    public List<InPlayMatch> getInPlayMatchList() {
        return inPlayMatchList;
    }

    public void setInPlayMatchList(List<InPlayMatch> inPlayMatchList) {
        this.inPlayMatchList = inPlayMatchList;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public List<InPlayContestMatchDto> getInPlayContestMatchDtoList() {
        return inPlayContestMatchDtoList;
    }

    public void setInPlayContestMatchDtoList(List<InPlayContestMatchDto> inPlayContestMatchDtoList) {
        this.inPlayContestMatchDtoList = inPlayContestMatchDtoList;
    }

}
