package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.webservice.UserReferralInfo;

/**
 * Created by deepanshi on 6/27/17.
 */

@Parcel
public class ReferralHistory {

    @SerializedName("type")
    private String referralHistoryType;

    @SerializedName("transaction_amount")
    private Integer transactionAmount;

    @SerializedName("order_id")
    private String referralOrderId;

    @SerializedName("info")
    private ReferralDetails referralDetails;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @SerializedName("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @SerializedName("transaction_amount")
    public Integer getTransactionAmount() {
        return transactionAmount;
    }

    @SerializedName("transaction_amount")
    public void setTransactionAmount(Integer transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @SerializedName("info")
    public ReferralDetails getReferralDetails() {
        return referralDetails;
    }

    @SerializedName("info")
    public void setReferralDetails(ReferralDetails referralDetails) {
        this.referralDetails = referralDetails;
    }

    @SerializedName("type")
    public String getReferralHistoryType() {
        return referralHistoryType;
    }

    @SerializedName("type")
    public void setReferralHistoryType(String referralHistoryType) {
        this.referralHistoryType = referralHistoryType;
    }

    @SerializedName("order_id")
    public String getReferralOrderId() {
        return referralOrderId;
    }

    @SerializedName("order_id")
    public void setReferralOrderId(String referralOrderId) {
        this.referralOrderId = referralOrderId;
    }

    @SerializedName("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @SerializedName("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


}
