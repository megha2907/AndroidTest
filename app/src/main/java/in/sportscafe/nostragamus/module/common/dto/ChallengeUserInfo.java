package in.sportscafe.nostragamus.module.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants.Powerups;

/**
 * Created by deepanshi on 2/21/17.
 */
@Parcel
public class ChallengeUserInfo {

    @SerializedName("paid_amount")
    private Integer paidAmount;

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("powerups_from_bank")
    private HashMap<String, Integer> withdrawnPowerUps = new HashMap<>();

    @SerializedName("config_index")
    private Integer configIndex;

    @SerializedName("paid_amount")
    public Integer getPaidAmount() {
        return paidAmount;
    }

    @SerializedName("paid_amount")
    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    @SerializedName("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps = initPowerUps(powerUps);
    }

    @SerializedName("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @SerializedName("powerups_from_bank")
    public HashMap<String, Integer> getWithdrawnPowerUps() {
        return withdrawnPowerUps = initPowerUps(withdrawnPowerUps);
    }

    @SerializedName("powerups_from_bank")
    public void setWithdrawnPowerUps(HashMap<String, Integer> withdrawnPowerUps) {
        this.withdrawnPowerUps = withdrawnPowerUps;
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

    @SerializedName("config_index")
    public Integer getConfigIndex() {
        return configIndex;
    }

    @SerializedName("config_index")
    public void setConfigIndex(Integer configIndex) {
        this.configIndex = configIndex;
    }


    public boolean isUserJoined() {
        return null != configIndex;
    }
}