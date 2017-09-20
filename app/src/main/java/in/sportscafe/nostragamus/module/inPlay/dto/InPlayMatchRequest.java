package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchRequest {

    @SerializedName("room_id")
    private int roomId;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
