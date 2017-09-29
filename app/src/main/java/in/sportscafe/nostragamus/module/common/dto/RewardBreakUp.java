package in.sportscafe.nostragamus.module.common.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 03/04/17.
 */
@Parcel
public class RewardBreakUp {

    @SerializedName("rank")
    private String rank;

    @SerializedName("amount")
    private String amount;

    @SerializedName("rank")
    public String getRank() {
        return rank;
    }

    @SerializedName("rank")
    public void setRank(String rank) {
        this.rank = rank;
    }

    @SerializedName("amount")
    public String getAmount() {
        return amount;
    }

    @SerializedName("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

}