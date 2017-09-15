package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 4/20/17.
 */

@Parcel
public class WinnersRewards {

    @SerializedName("rank")
    private String winnerRank;

    @SerializedName("amount")
    private String amountWon;

    @SerializedName("user_nick")
    private String winnerName;

    @SerializedName("rank")
    public String getWinnerRank() {
        return winnerRank;
    }

    @SerializedName("rank")
    public void setWinnerRank(String winnerRank) {
        this.winnerRank = winnerRank;
    }

    @SerializedName("amount")
    public String getAmountWon() {
        return amountWon;
    }

    @SerializedName("amount")
    public void setAmountWon(String amountWon) {
        this.amountWon = amountWon;
    }

    @SerializedName("user_nick")
    public String getWinnerName() {
        return winnerName;
    }

    @SerializedName("user_nick")
    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
}
