package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletHistoryTransaction {

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("trasaction_status")
    private String trasactionStatus;

    @JsonProperty("account")
    private String account;

    @JsonProperty("message")
    private String message;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("money_flow")
    private String moneyFlow;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTrasactionStatus() {
        return trasactionStatus;
    }

    public void setTrasactionStatus(String trasactionStatus) {
        this.trasactionStatus = trasactionStatus;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(String moneyFlow) {
        this.moneyFlow = moneyFlow;
    }
}
