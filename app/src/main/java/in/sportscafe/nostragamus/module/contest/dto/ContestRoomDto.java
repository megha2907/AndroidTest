package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestRoomDto {

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("room_size")
    private int roomSize;

    @SerializedName("room_status")
    private String roomStatus;

    @SerializedName("room_name")
    private String roomTitle;

    @SerializedName("entries_size")
    private int entriesSize;

    @SerializedName("entries")
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

    public int getEntriesSize() {
        return entriesSize;
    }

    public void setEntriesSize(int entriesSize) {
        this.entriesSize = entriesSize;
    }
}
