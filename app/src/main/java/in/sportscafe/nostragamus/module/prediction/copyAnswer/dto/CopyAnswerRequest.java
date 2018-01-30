package in.sportscafe.nostragamus.module.prediction.copyAnswer.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 25/1/18.
 */

public class CopyAnswerRequest {

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("source_room_id")
    private int sourceRoomId;

    @SerializedName("target_room_id")
    private int targetRoomId;

    @SerializedName("copy_powerups")
    private boolean shouldCopyPowerups = true;  // Default true

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getSourceRoomId() {
        return sourceRoomId;
    }

    public void setSourceRoomId(int sourceRoomId) {
        this.sourceRoomId = sourceRoomId;
    }

    public int getTargetRoomId() {
        return targetRoomId;
    }

    public void setTargetRoomId(int targetRoomId) {
        this.targetRoomId = targetRoomId;
    }

    public boolean isShouldCopyPowerups() {
        return shouldCopyPowerups;
    }

    public void setShouldCopyPowerups(boolean shouldCopyPowerups) {
        this.shouldCopyPowerups = shouldCopyPowerups;
    }
}
