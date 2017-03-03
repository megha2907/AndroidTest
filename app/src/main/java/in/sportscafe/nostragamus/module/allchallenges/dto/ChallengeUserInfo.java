package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants.Powerups;

/**
 * Created by deepanshi on 2/21/17.
 */
@Parcel
public class ChallengeUserInfo {

    @JsonProperty("paid_amount")
    private Integer paidAmount;

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("powerups_from_bank")
    private HashMap<String, Integer> withdrawnPowerUps = new HashMap<>();

    @JsonProperty("maxTransferLimit")
    private int maxWithdrawLimit = 0;

    @JsonProperty("paid_amount")
    public Integer getPaidAmount() {
        return paidAmount;
    }

    @JsonProperty("paid_amount")
    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps = initPowerUps(powerUps);
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @JsonProperty("powerups_from_bank")
    public HashMap<String, Integer> getWithdrawnPowerUps() {
        return withdrawnPowerUps = initPowerUps(withdrawnPowerUps);
    }

    @JsonProperty("powerups_from_bank")
    public void setWithdrawnPowerUps(HashMap<String, Integer> withdrawnPowerUps) {
        this.withdrawnPowerUps = withdrawnPowerUps;
    }

    @JsonProperty("maxTransferLimit")
    public int getMaxWithdrawLimit() {
        return maxWithdrawLimit;
    }

    @JsonProperty("maxTransferLimit")
    public void setMaxWithdrawLimit(int maxWithdrawLimit) {
        this.maxWithdrawLimit = maxWithdrawLimit;
    }

    @JsonIgnore
    private HashMap<String, Integer> initPowerUps(HashMap<String, Integer> powerUpMap) {
        if(null == powerUpMap) {
            powerUpMap = new HashMap<>();
        }

        boolean isEmptyInitially = powerUpMap.size() == 0;

        if(isEmptyInitially || !powerUpMap.containsKey(Powerups.XX)) {
            powerUpMap.put(Powerups.XX, 0);
        }

        if(isEmptyInitially || !powerUpMap.containsKey(Powerups.NO_NEGATIVE)) {
            powerUpMap.put(Powerups.NO_NEGATIVE, 0);
        }

        if(isEmptyInitially || !powerUpMap.containsKey(Powerups.AUDIENCE_POLL)) {
            powerUpMap.put(Powerups.AUDIENCE_POLL, 0);
        }

        return powerUpMap;
    }
}