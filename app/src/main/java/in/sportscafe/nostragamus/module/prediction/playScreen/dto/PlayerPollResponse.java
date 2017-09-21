package in.sportscafe.nostragamus.module.prediction.playScreen.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayerPollResponse {

    @SerializedName("data")
    private List<PlayersPoll> playersPollList;

    @SerializedName("data")
    public List<PlayersPoll> getPlayersPollList() {
        return playersPollList;
    }

    @SerializedName("data")
    public void setPlayersPollList(List<PlayersPoll> playersPollList) {
        this.playersPollList = playersPollList;
    }

}