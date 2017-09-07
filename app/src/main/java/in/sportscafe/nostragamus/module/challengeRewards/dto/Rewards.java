package in.sportscafe.nostragamus.module.challengeRewards.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlay;

/**
 * Created by deepanshi on 9/6/17.
 */

@Parcel
public class Rewards {

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("userName")
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
