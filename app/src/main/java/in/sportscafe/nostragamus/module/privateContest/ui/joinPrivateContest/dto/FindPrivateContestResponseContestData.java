package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sc on 27/3/18.
 */
@Parcel
public class FindPrivateContestResponseContestData {

    @SerializedName("config_id")
    private int configId;

    @SerializedName("config_name")
    private String configName;

    @SerializedName("fee")
    private int fee;

    @SerializedName("priority")
    private int priority;

    @SerializedName("prize_money")
    private int prizeMoney;

    @SerializedName("max_participants")
    private int maxParticipants;

    @SerializedName("mode")
    private String mode;

    @SerializedName("filling_rooms")
    private int fillingRooms;

    @SerializedName("filled_rooms")
    private int filledRooms;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("contest_type")
    private String contestType;

    @SerializedName("joined_contest")
    private boolean joinedContest;

    @SerializedName("joinable")
    private boolean joinable;

    @SerializedName("maxTransferLimit")
    private int maxTransferLimit;

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPrizeMoney() {
        return prizeMoney;
    }

    public void setPrizeMoney(int prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getFillingRooms() {
        return fillingRooms;
    }

    public void setFillingRooms(int fillingRooms) {
        this.fillingRooms = fillingRooms;
    }

    public int getFilledRooms() {
        return filledRooms;
    }

    public void setFilledRooms(int filledRooms) {
        this.filledRooms = filledRooms;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContestType() {
        return contestType;
    }

    public void setContestType(String contestType) {
        this.contestType = contestType;
    }

    public boolean isJoinedContest() {
        return joinedContest;
    }

    public void setJoinedContest(boolean joinedContest) {
        this.joinedContest = joinedContest;
    }

    public boolean isJoinable() {
        return joinable;
    }

    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public int getMaxTransferLimit() {
        return maxTransferLimit;
    }

    public void setMaxTransferLimit(int maxTransferLimit) {
        this.maxTransferLimit = maxTransferLimit;
    }
}
