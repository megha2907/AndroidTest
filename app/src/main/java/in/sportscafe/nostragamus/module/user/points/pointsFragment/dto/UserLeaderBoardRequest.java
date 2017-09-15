package in.sportscafe.nostragamus.module.user.points.pointsFragment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 9/15/17.
 */

public class UserLeaderBoardRequest {

    @JsonProperty("room_id")
    private int roomId;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
