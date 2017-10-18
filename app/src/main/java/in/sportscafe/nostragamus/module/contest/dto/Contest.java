package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterItemType;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 01/09/17.
 */

@Parcel
public class Contest {

    @SerializedName("config_id")
    private int contestId;

    @SerializedName("config_name")
    private String configName;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("prize_money")
    private int prizes;

    @SerializedName("contest_type")
    private ContestType contestType;

    @SerializedName("mode")
    private String contestMode;

    @SerializedName("fee")
    private int entryFee;

    @SerializedName("max_participants")
    private int roomSize;

    @SerializedName("filled_rooms")
    private int filledRooms;

    @SerializedName("filling_rooms")
    private int fillingRooms;

    @SerializedName("max_transfer_limit")
    private int maxTransferPowerUps;

    @SerializedName("tot_matches")
    private int totalMatches;

    @SerializedName("tournaments")
    private List<String> tournaments = new ArrayList<>();

    @SerializedName("powerups")
    private PowerUp powerUpInfo;

    @SerializedName("joinable")
    private boolean isJoinable;

    @SerializedName("joined_contest")
    private boolean isContestJoined;

    private int contestItemType = ContestAdapterItemType.CONTEST;

    private int challengeId;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getPrizes() {
        return prizes;
    }

    public void setPrizes(int prizes) {
        this.prizes = prizes;
    }

    public int getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(int entryFee) {
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


    public boolean isFreeEntry() {
        return entryFee == 0;
    }


    public boolean isUnlimitedEntries() {
        return roomSize == -1;
    }


    public boolean noPrizes() {
        return prizes == 0;
    }

    public void setPrizes(Integer prizes) {
        this.prizes = prizes;
    }

    public ContestType getContestType() {
        return contestType;
    }

    public void setContestType(ContestType contestType) {
        this.contestType = contestType;
    }
//
//    public void setRoomSize(Integer roomSize) {
//        this.roomSize = roomSize;
//    }
//}

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getContestMode() {
        return contestMode;
    }

    public void setContestMode(String contestMode) {
        this.contestMode = contestMode;
    }

    public PowerUp getPowerUp() {
        return powerUpInfo;
    }

    public void setPowerUp(PowerUp powerUpInfo) {
        this.powerUpInfo = powerUpInfo;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public boolean isContestJoined() {
        return isContestJoined;
    }

    public void setContestJoined(boolean contestJoined) {
        isContestJoined = contestJoined;
    }

    public boolean isJoinable() {
        return isJoinable;
    }

    public void setJoinable(boolean joinable) {
        isJoinable = joinable;
    }
}
