package in.sportscafe.nostragamus.module.challengeRewards.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


/**
 * Created by deepanshi on 9/6/17.
 */

@Parcel
public class Rewards {

    @SerializedName("rank")
    private String rank;

    @SerializedName("amount")
    private Integer amount;

    @SerializedName("user_nick")
    private String userName;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
