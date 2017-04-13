package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 8/4/17.
 */

public class PaytmTransactionResponse {

    String mid;
    String orderId;
    String transactionId;
    String bankTransactionId;
    String transactionAmount;
    String status;
    String currency;
    String responseCode;
    String responseMessage;
    String transactionDate;
    String gatewayName;
    String bankName;
    String paymentMode;
    String checkSumHash;
    JoinedChallengeInfo joinedChallengeInfo;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCheckSumHash() {
        return checkSumHash;
    }

    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }

    public JoinedChallengeInfo getJoinedChallengeInfo() {
        return joinedChallengeInfo;
    }

    public void setJoinedChallengeInfo(JoinedChallengeInfo joinedChallengeInfo) {
        this.joinedChallengeInfo = joinedChallengeInfo;
    }
}
