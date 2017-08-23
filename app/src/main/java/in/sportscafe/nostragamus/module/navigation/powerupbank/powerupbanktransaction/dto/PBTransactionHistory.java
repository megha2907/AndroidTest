package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralDetails;

/**
 * Created by deepanshi on 7/13/17.
 */
@Parcel
public class PBTransactionHistory {

    @JsonProperty("type")
    private String type;

    @JsonProperty("info")
    private TransactionTypeInfo transactionTypeInfo;

    @JsonProperty("createdAt")
    private String createdAt;

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


    @JsonProperty("info")
    public TransactionTypeInfo getTransactionTypeInfo() {
        return transactionTypeInfo;
    }

    @JsonProperty("info")
    public void setTransactionTypeInfo(TransactionTypeInfo transactionTypeInfo) {
        this.transactionTypeInfo = transactionTypeInfo;
    }


    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
