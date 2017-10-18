package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 21/07/17.
 */

public class PowerupBankStatusRequest {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("room_id")
    private int roomId;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
