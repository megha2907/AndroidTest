package in.sportscafe.nostragamus.module.common.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/21/17.
 */
@Parcel
public class PaymentInfo {

    @SerializedName("fee")
    private Integer challengeFee;

    @SerializedName("prize_money")
    private Integer prizeMoney;

    @SerializedName("fee")
    public Integer getChallengeFee() {
        return challengeFee;
    }

    @SerializedName("fee")
    public void setChallengeFee(Integer challengeFee) {
        this.challengeFee = challengeFee;
    }

    @SerializedName("prize_money")
    public Integer getPrizeMoney() {
        return prizeMoney;
    }

    @SerializedName("prize_money")
    public void setPrizeMoney(Integer prizeMoney) {
        this.prizeMoney = prizeMoney;
    }


}
