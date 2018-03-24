package in.sportscafe.nostragamus.module.privateContest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 23/3/18.
 */

public class PrivateContestPrizeResponse {

    @SerializedName("winnerRank")
    private int winnerRank;

    @SerializedName("sharePercentage")
    private float sharePercentage;

    @SerializedName("winnerPercentage")
    private float winningPercentage;

    public int getWinnerRank() {
        return winnerRank;
    }

    public void setWinnerRank(int winnerRank) {
        this.winnerRank = winnerRank;
    }

    public float getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(float sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public float getWinningPercentage() {
        return winningPercentage;
    }

    public void setWinningPercentage(float winningPercentage) {
        this.winningPercentage = winningPercentage;
    }
}
