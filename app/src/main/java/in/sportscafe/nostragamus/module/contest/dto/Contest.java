package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterItemType;

/**
 * Created by sandip on 01/09/17.
 */

public class Contest {

    @JsonProperty("contest_id")
    private int contestId;

    @JsonProperty("contest_type_id")
    private int contestTypeId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("category")
    private String category;

    @JsonProperty("prizes")
    private int prizes;

    @JsonProperty("entry_fee")
    private double entryFee;

    @JsonProperty("room_size")
    private int roomSize;

    @JsonProperty("filled_rooms")
    private int filledRooms;

    @JsonProperty("filling_rooms")
    private int fillingRooms;

    private int contestItemType = ContestAdapterItemType.CONTEST;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getContestTypeId() {
        return contestTypeId;
    }

    public void setContestTypeId(int contestTypeId) {
        this.contestTypeId = contestTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrizes() {
        return prizes;
    }

    public void setPrizes(int prizes) {
        this.prizes = prizes;
    }

    public double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public int getFilledRooms() {
        return filledRooms;
    }

    public void setFilledRooms(int filledRooms) {
        this.filledRooms = filledRooms;
    }

    public int getFillingRooms() {
        return fillingRooms;
    }

    public void setFillingRooms(int fillingRooms) {
        this.fillingRooms = fillingRooms;
    }

    public int getContestItemType() {
        return contestItemType;
    }

    public void setContestItemType(int contestItemType) {
        this.contestItemType = contestItemType;
    }
}
