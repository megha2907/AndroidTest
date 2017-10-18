package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 23/09/17.
 */

public class JoinPseudoContestResponse {

    @SerializedName("userRoom")
    private JoinPseudoContestResponseData userRoom;

    public JoinPseudoContestResponseData getUserRoom() {
        return userRoom;
    }

    public void setUserRoom(JoinPseudoContestResponseData userRoom) {
        this.userRoom = userRoom;
    }
}
