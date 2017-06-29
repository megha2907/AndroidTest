package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.webservice.UserReferralInfo;

/**
 * Created by deepanshi on 6/27/17.
 */

@Parcel
public class ReferralHistory {

    @JsonProperty("type")
    private String referralHistoryType;

    @JsonProperty("transaction_amount")
    private Integer transactionAmount;

    @JsonProperty("order_id")
    private String referralOrderId;

    @JsonProperty("info")
    private ReferralDetails referralDetails;

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

    @JsonProperty("transaction_amount")
    public Integer getTransactionAmount() {
        return transactionAmount;
    }

    @JsonProperty("transaction_amount")
    public void setTransactionAmount(Integer transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @JsonProperty("info")
    public ReferralDetails getReferralDetails() {
        return referralDetails;
    }

    @JsonProperty("info")
    public void setReferralDetails(ReferralDetails referralDetails) {
        this.referralDetails = referralDetails;
    }

    @JsonProperty("type")
    public String getReferralHistoryType() {
        return referralHistoryType;
    }

    @JsonProperty("type")
    public void setReferralHistoryType(String referralHistoryType) {
        this.referralHistoryType = referralHistoryType;
    }

    @JsonProperty("order_id")
    public String getReferralOrderId() {
        return referralOrderId;
    }

    @JsonProperty("order_id")
    public void setReferralOrderId(String referralOrderId) {
        this.referralOrderId = referralOrderId;
    }


}
