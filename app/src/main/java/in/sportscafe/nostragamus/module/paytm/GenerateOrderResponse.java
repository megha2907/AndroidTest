package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 7/4/17.
 */

public class GenerateOrderResponse {

    @JsonProperty("CALLBACK_URL")
    private String callbackUrl;

    @JsonProperty("CHANNEL_ID")
    private String challengeId;

    @JsonProperty("CUST_ID")
    private int custId;

    @JsonProperty("EMAIL")
    private String email;

    @JsonProperty("INDUSTRY_TYPE_ID")
    private String industryTypeId;

    @JsonProperty("MID")
    private String mId;

    @JsonProperty("MOBILE_NO")
    private String mobileNo;

    @JsonProperty("ORDER_ID")
    private String orderId;

    @JsonProperty("REQUEST_TYPE")
    private String requestType;

    @JsonProperty("TXN_AMOUNT")
    private int txnAmount;

    @JsonProperty("WEBSITE")
    private String website;

    @JsonProperty("CHECKSUMHASH")
    private String checkSumHash;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndustryTypeId() {
        return industryTypeId;
    }

    public void setIndustryTypeId(String industryTypeId) {
        this.industryTypeId = industryTypeId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(int txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCheckSumHash() {
        return checkSumHash;
    }

    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }
}
