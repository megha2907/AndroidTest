package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestEntriesResponse {

    @SerializedName("filled")
    private List<ContestRoomDto> filledRooms = null;

    @SerializedName("filling")
    private List<ContestRoomDto> fillingRooms = null;

    public List<ContestRoomDto> getFilledRooms() {
        return filledRooms;
    }

    public void setFilledRooms(List<ContestRoomDto> filledRooms) {
        this.filledRooms = filledRooms;
    }

    public List<ContestRoomDto> getFillingRooms() {
        return fillingRooms;
    }

    public void setFillingRooms(List<ContestRoomDto> fillingRooms) {
        this.fillingRooms = fillingRooms;
    }
}
