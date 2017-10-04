package in.sportscafe.nostragamus.module.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants.Powerups;

/**
 * Created by deepanshi on 2/21/17.
 */
@Parcel
public class ChallengeInfo {

    @SerializedName("payment_info")
    private PaymentInfo paymentInfo;

    @SerializedName("maxTransferLimit")
    private int maxWithdrawLimit = 0;

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("configs")
    private List<ChallengeConfig> configs = new ArrayList<>();

    @SerializedName("payment_info")
    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    @SerializedName("isClosed")
    private boolean isClosed;

    @SerializedName("payment_info")
    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @SerializedName("maxTransferLimit")
    public int getMaxWithdrawLimit() {
        return maxWithdrawLimit;
    }

    @SerializedName("maxTransferLimit")
    public void setMaxWithdrawLimit(int maxWithdrawLimit) {
        this.maxWithdrawLimit = maxWithdrawLimit;
    }

    @SerializedName("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps = initPowerUps(powerUps);
    }

    @SerializedName("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @SerializedName("configs")
    public List<ChallengeConfig> getConfigs() {
        return configs;
    }

    @SerializedName("configs")
    public void setConfigs(List<ChallengeConfig> configs) {
        this.configs = configs;
    }

    @SerializedName("isClosed")
    public boolean isClosed() {
        return isClosed;
    }

    @SerializedName("isClosed")
    public void setClosed(boolean closed) {
        isClosed = closed;
    }


    private HashMap<String, Integer> initPowerUps(HashMap<String, Integer> powerUpMap) {
        if (null == powerUpMap) {
            powerUpMap = new HashMap<>();
        }

        boolean isEmptyInitially = powerUpMap.size() == 0;

        if (isEmptyInitially || !powerUpMap.containsKey(Powerups.XX)) {
            powerUpMap.put(Powerups.XX, 0);
        }

        if (isEmptyInitially || !powerUpMap.containsKey(Powerups.NO_NEGATIVE)) {
            powerUpMap.put(Powerups.NO_NEGATIVE, 0);
        }

        if (isEmptyInitially || !powerUpMap.containsKey(Powerups.AUDIENCE_POLL)) {
            powerUpMap.put(Powerups.AUDIENCE_POLL, 0);
        }

        return powerUpMap;
    }
}