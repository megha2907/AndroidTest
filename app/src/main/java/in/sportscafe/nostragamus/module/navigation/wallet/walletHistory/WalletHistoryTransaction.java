package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 12/04/17.
 */

public class WalletHistoryTransaction {

    @SerializedName("transaction_type")
    private String transactionType;

    @SerializedName("trasaction_status")
    private String trasactionStatus;

    @SerializedName("account")
    private String account;

    @SerializedName("message")
    private String message;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("amount")
    private double amount;

    @SerializedName("money_flow")
    private String moneyFlow;

    @SerializedName("show_flag")
    private boolean showReportButton;

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

    public boolean isShowReportButton() {
        return showReportButton;
    }

    public void setShowReportButton(boolean showReportButton) {
        this.showReportButton = showReportButton;
    }
}
