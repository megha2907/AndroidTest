package in.sportscafe.nostragamus.module.privateContest.dto;

import org.parceler.Parcel;

/**
 * Created by sc on 23/3/18.
 */
@Parcel
public class PrizeListItemDto {

    private int winnerRank;
    private double amount;
    private float sharePercent;

    public int getWinnerRank() {
        return winnerRank;
    }

    public void setWinnerRank(int winnerRank) {
        this.winnerRank = winnerRank;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public float getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(float sharePercent) {
        this.sharePercent = sharePercent;
    }
}
