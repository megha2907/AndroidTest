package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

/**
 * Created by deepanshi on 2/21/17.
 */
@Parcel
public class ChallengeInfo {

    @JsonProperty("payment_info")
    private PaymentInfo paymentInfo;

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @JsonProperty("payment_info")
    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    @JsonProperty("payment_info")
    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

}
