package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 03/04/17.
 */
@Parcel
public class RewardBreakUp {

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("winner_name")
    private String winnerName;

    @JsonProperty("rank")
    public String getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(String rank) {
        this.rank = rank;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("winner_name")
    public String getWinnerName() {
        return winnerName;
    }

    @JsonProperty("winner_name")
    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
}