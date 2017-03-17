package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/21/17.
 */
@Parcel
public class PaymentInfo {

    @JsonProperty("fee")
    private Integer challengeFee;

    @JsonProperty("prize_money")
    private Integer prizeMoney;

    @JsonProperty("fee")
    public Integer getChallengeFee() {
        return challengeFee;
    }

    @JsonProperty("fee")
    public void setChallengeFee(Integer challengeFee) {
        this.challengeFee = challengeFee;
    }

    @JsonProperty("prize_money")
    public Integer getPrizeMoney() {
        return prizeMoney;
    }

    @JsonProperty("prize_money")
    public void setPrizeMoney(Integer prizeMoney) {
        this.prizeMoney = prizeMoney;
    }


}
