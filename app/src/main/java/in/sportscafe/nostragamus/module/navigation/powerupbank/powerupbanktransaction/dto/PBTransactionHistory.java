package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralDetails;

/**
 * Created by deepanshi on 7/13/17.
 */
@Parcel
public class PBTransactionHistory {

    @SerializedName("type")
    private String type;

    @SerializedName("info")
    private TransactionTypeInfo transactionTypeInfo;

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


    @SerializedName("info")
    public TransactionTypeInfo getTransactionTypeInfo() {
        return transactionTypeInfo;
    }

    @SerializedName("info")
    public void setTransactionTypeInfo(TransactionTypeInfo transactionTypeInfo) {
        this.transactionTypeInfo = transactionTypeInfo;
    }


    @SerializedName("type")
    public String getType() {
        return type;
    }

    @SerializedName("type")
    public void setType(String type) {
        this.type = type;
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
