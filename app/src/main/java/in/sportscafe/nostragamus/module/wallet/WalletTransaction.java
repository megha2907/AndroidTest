package in.sportscafe.nostragamus.module.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletTransaction {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("challenge_id")
    private Integer challengeId;

    @JsonProperty("money_flow")
    private String moneyFlow;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    public String getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(String moneyFlow) {
        this.moneyFlow = moneyFlow;
    }
}
