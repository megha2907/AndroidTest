package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestRoomDto {

    @JsonProperty("room_id")
    private int roomId;

    @JsonProperty("room_size")
    private int roomSize;

    @JsonProperty("room_status")
    private String roomStatus;

    @JsonProperty("room_title")
    private String roomTitle;

    @JsonProperty("entries")
    private List<ContestRoomEntryDto> entries = null;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public List<ContestRoomEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<ContestRoomEntryDto> entries) {
        this.entries = entries;
    }
}
