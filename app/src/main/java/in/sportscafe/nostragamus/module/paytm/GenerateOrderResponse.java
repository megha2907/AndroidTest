package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;

/**
 * Created by sandip on 7/4/17.
 */
@Parcel
public class GenerateOrderResponse {

    @JsonProperty("CALLBACK_URL")
    private String callbackUrl;

    @JsonProperty("CHANNEL_ID")
    private String channelId;

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

    @JsonProperty("joined_challenge_info")
    private JoinedChallengeInfo joinedChallengeInfo;

    @JsonProperty("CALLBACK_URL")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    @JsonProperty("CALLBACK_URL")
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @JsonProperty("CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    @JsonProperty("CHANNEL_ID")
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @JsonProperty("CUST_ID")
    public int getCustId() {
        return custId;
    }

    @JsonProperty("CUST_ID")
    public void setCustId(int custId) {
        this.custId = custId;
    }

    @JsonProperty("EMAIL")
    public String getEmail() {
        return email;
    }

    @JsonProperty("EMAIL")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("INDUSTRY_TYPE_ID")
    public String getIndustryTypeId() {
        return industryTypeId;
    }

    @JsonProperty("INDUSTRY_TYPE_ID")
    public void setIndustryTypeId(String industryTypeId) {
        this.industryTypeId = industryTypeId;
    }

    @JsonProperty("MID")
    public String getmId() {
        return mId;
    }

    @JsonProperty("MID")
    public void setmId(String mId) {
        this.mId = mId;
    }

    @JsonProperty("MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    @JsonProperty("MOBILE_NO")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @JsonProperty("ORDER_ID")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("ORDER_ID")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("REQUEST_TYPE")
    public String getRequestType() {
        return requestType;
    }

    @JsonProperty("REQUEST_TYPE")
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @JsonProperty("TXN_AMOUNT")
    public int getTxnAmount() {
        return txnAmount;
    }

    @JsonProperty("TXN_AMOUNT")
    public void setTxnAmount(int txnAmount) {
        this.txnAmount = txnAmount;
    }

    @JsonProperty("WEBSITE")
    public String getWebsite() {
        return website;
    }

    @JsonProperty("WEBSITE")
    public void setWebsite(String website) {
        this.website = website;
    }

    @JsonProperty("CHECKSUMHASH")
    public String getCheckSumHash() {
        return checkSumHash;
    }

    @JsonProperty("CHECKSUMHASH")
    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }

    @JsonProperty("joined_challenge_info")
    public JoinedChallengeInfo getJoinedChallengeInfo() {
        return joinedChallengeInfo;
    }

    @JsonProperty("joined_challenge_info")
    public void setJoinedChallengeInfo(JoinedChallengeInfo joinedChallengeInfo) {
        this.joinedChallengeInfo = joinedChallengeInfo;
    }

}
