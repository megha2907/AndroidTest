package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 12/09/17.
 */
@Parcel
public class InPlayContestDto {

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("contest_tab_id")
    private int contestTabId;

    @SerializedName("contest_mode")
    private String contestMode;

    @SerializedName("config_name")
    private String contestName;

    @SerializedName("rank")
    private int rank;

    @SerializedName("total_participants")
    private int totalParticipants;

    @SerializedName("winning_amount")
    private int winningAmount;

    @SerializedName("entry_fee")
    private int entryFee;

    @SerializedName("powerups")
    private PowerUp powerUp;

    @SerializedName("headless_state")
    private boolean headlessState;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("matches")
    private List<InPlayContestMatchDto> matches = null;

    private int challengeId;
    private String challengeName;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getContestTabId() {
        return contestTabId;
    }

    public void setContestTabId(int contestTabId) {
        this.contestTabId = contestTabId;
    }

    public String getContestMode() {
        return contestMode;
    }

    public void setContestMode(String contestMode) {
        this.contestMode = contestMode;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public int getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(int winningAmount) {
        this.winningAmount = winningAmount;
    }

    public int getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public List<InPlayContestMatchDto> getMatches() {
        return matches;
    }

    public void setMatches(List<InPlayContestMatchDto> matches) {
        this.matches = matches;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isHeadlessState() {
        return headlessState;
    }

    public void setHeadlessState(boolean headlessState) {
        this.headlessState = headlessState;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }
}
