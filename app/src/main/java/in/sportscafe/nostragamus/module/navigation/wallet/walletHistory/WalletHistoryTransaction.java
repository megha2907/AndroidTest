package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletHistoryTransaction {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("money_flow")
    private String moneyFlow;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("rank")
    private String rank;

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

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(String moneyFlow) {
        this.moneyFlow = moneyFlow;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
