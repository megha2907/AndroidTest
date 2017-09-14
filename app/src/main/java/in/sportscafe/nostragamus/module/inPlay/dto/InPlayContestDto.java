package in.sportscafe.nostragamus.module.inPlay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by sandip on 12/09/17.
 */
@Parcel
public class InPlayContestDto {

    @JsonProperty("contest_id")
    private int contestId;

    @JsonProperty("contest_tab_id")
    private int contestTabId;

    @JsonProperty("contest_mode")
    private String contestMode;

    @JsonProperty("contest_name")
    private String contestName;

    @JsonProperty("rank")
    private int rank;

    @JsonProperty("total_participants")
    private int totalParticipants;

    @JsonProperty("winning_amount")
    private int winningAmount;

    @JsonProperty("entry_fee")
    private int entryFee;

    @JsonProperty("matches")
    private List<InPlayContestMatchDto> matches = null;

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

    public List<InPlayContestMatchDto> getMatches() {
        return matches;
    }

    public void setMatches(List<InPlayContestMatchDto> matches) {
        this.matches = matches;
    }
}
