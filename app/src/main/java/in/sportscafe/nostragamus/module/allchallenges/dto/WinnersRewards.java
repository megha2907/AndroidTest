package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 4/20/17.
 */

@Parcel
public class WinnersRewards {

    @JsonProperty("rank")
    private String winnerRank;

    @JsonProperty("amount")
    private String amountWon;

    @JsonProperty("user_nick")
    private String winnerName;

    @JsonProperty("rank")
    public String getWinnerRank() {
        return winnerRank;
    }

    @JsonProperty("rank")
    public void setWinnerRank(String winnerRank) {
        this.winnerRank = winnerRank;
    }

    @JsonProperty("amount")
    public String getAmountWon() {
        return amountWon;
    }

    @JsonProperty("amount")
    public void setAmountWon(String amountWon) {
        this.amountWon = amountWon;
    }

    @JsonProperty("user_nick")
    public String getWinnerName() {
        return winnerName;
    }

    @JsonProperty("user_nick")
    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
}
