package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterItemType;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Tournament;

/**
 * Created by sandip on 01/09/17.
 */

@Parcel
public class Contest {

    @JsonProperty("config_id")
    private int contestId;

    @JsonProperty("config_name")
    private String configName;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("prize_money")
    private Integer prizes;

    @JsonProperty("contest_type")
    private ContestTypeInfo contestTypeInfo;

    @JsonProperty("mode")
    private ContestModeInfo contestModeInfo;

    @JsonProperty("fee")
    private Integer entryFee;

    @JsonProperty("max_participants")
    private Integer roomSize;

    @JsonProperty("filled_rooms")
    private int filledRooms;

    @JsonProperty("filling_rooms")
    private int fillingRooms;

    @JsonProperty("max_transfer_limit")
    private int maxTransferPowerUps;

    @JsonProperty("tot_matches")
    private int totalMatches;

    @JsonProperty("tournaments")
    private List<String> tournaments = new ArrayList<>();

    @JsonProperty("powerups")
    HashMap<String, Integer> powerUpsMap = new HashMap<String, Integer>();

    private int contestItemType = ContestAdapterItemType.CONTEST;

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

    public Integer getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Integer entryFee) {
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

    @JsonIgnore
    public boolean isFreeEntry() {
        return entryFee == 0 || entryFee == null;
    }

    @JsonIgnore
    public boolean isUnlimitedEntries() {
        return roomSize == 0 || roomSize == null;
    }

    @JsonIgnore
    public boolean noPrizes() {
        return prizes == 0 || prizes == null;
    }

    public void setPrizes(Integer prizes) {
        this.prizes = prizes;
    }

    public ContestTypeInfo getContestTypeInfo() {
        return contestTypeInfo;
    }

    public void setContestTypeInfo(ContestTypeInfo contestTypeInfo) {
        this.contestTypeInfo = contestTypeInfo;
    }
//
//    public void setRoomSize(Integer roomSize) {
//        this.roomSize = roomSize;
//    }
//}

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getMaxTransferPowerUps() {
        return maxTransferPowerUps;
    }

    public void setMaxTransferPowerUps(int maxTransferPowerUps) {
        this.maxTransferPowerUps = maxTransferPowerUps;
    }

    public List<String> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<String> tournaments) {
        this.tournaments = tournaments;
    }


    public HashMap<String, Integer> getPowerUpsMap() {
        return powerUpsMap;
    }

    public void setPowerUpsMap(HashMap<String, Integer> powerUpsMap) {
        this.powerUpsMap = powerUpsMap;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public ContestModeInfo getContestModeInfo() {
        return contestModeInfo;
    }

    public void setContestModeInfo(ContestModeInfo contestModeInfo) {
        this.contestModeInfo = contestModeInfo;
    }


}
