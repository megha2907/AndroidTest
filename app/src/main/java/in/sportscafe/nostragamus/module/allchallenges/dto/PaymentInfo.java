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

    @JsonProperty("fee")
    public Integer getChallengeFee() {
        return challengeFee;
    }

    @JsonProperty("fee")
    public void setChallengeFee(Integer challengeFee) {
        this.challengeFee = challengeFee;
    }

}
