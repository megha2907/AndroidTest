package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("payment_info")
    private PaymentInfo paymentInfo;

    @JsonProperty("maxTransferLimit")
    private int maxWithdrawLimit = 0;

    @JsonProperty("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("configs")
    private List<ChallengeConfig> configs = new ArrayList<>();

    @JsonProperty("payment_info")
    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    @JsonProperty("prize_money_topline")
    private String prizeMoneyTopline;

    @JsonProperty("isClosed")
    private boolean isClosed;

    @JsonProperty("payment_info")
    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @JsonProperty("maxTransferLimit")
    public int getMaxWithdrawLimit() {
        return maxWithdrawLimit;
    }

    @JsonProperty("maxTransferLimit")
    public void setMaxWithdrawLimit(int maxWithdrawLimit) {
        this.maxWithdrawLimit = maxWithdrawLimit;
    }

    @JsonProperty("powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps = initPowerUps(powerUps);
    }

    @JsonProperty("powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    @JsonProperty("configs")
    public List<ChallengeConfig> getConfigs() {
        return configs;
    }

    @JsonProperty("configs")
    public void setConfigs(List<ChallengeConfig> configs) {
        this.configs = configs;
    }

    @JsonProperty("prize_money_topline")
    public String getPrizeMoneyTopline() {
        return prizeMoneyTopline;
    }

    @JsonProperty("prize_money_topline")
    public void setPrizeMoneyTopline(String prizeMoneyTopline) {
        this.prizeMoneyTopline = prizeMoneyTopline;
    }

    @JsonProperty("isClosed")
    public boolean isClosed() {
        return isClosed;
    }

    @JsonProperty("isClosed")
    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @JsonIgnore
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