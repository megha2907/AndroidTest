package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 7/4/17.
 */
@Parcel
public class GenerateOrderResponse {

    @SerializedName("CALLBACK_URL")
    private String callbackUrl;

    @SerializedName("CHANNEL_ID")
    private String channelId;

    @SerializedName("CUST_ID")
    private int custId;

    @SerializedName("EMAIL")
    private String email;

    @SerializedName("INDUSTRY_TYPE_ID")
    private String industryTypeId;

    @SerializedName("MID")
    private String mId;

    @SerializedName("MOBILE_NO")
    private String mobileNo;

    @SerializedName("ORDER_ID")
    private String orderId;

    @SerializedName("REQUEST_TYPE")
    private String requestType;

    @SerializedName("TXN_AMOUNT")
    private int txnAmount;

    @SerializedName("WEBSITE")
    private String website;

    @SerializedName("CHECKSUMHASH")
    private String checkSumHash;

    @SerializedName("joined_challenge_info")
    private JoinedChallengeInfo joinedChallengeInfo;

    @SerializedName("CALLBACK_URL")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    @SerializedName("CALLBACK_URL")
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @SerializedName("CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    @SerializedName("CHANNEL_ID")
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @SerializedName("CUST_ID")
    public int getCustId() {
        return custId;
    }

    @SerializedName("CUST_ID")
    public void setCustId(int custId) {
        this.custId = custId;
    }

    @SerializedName("EMAIL")
    public String getEmail() {
        return email;
    }

    @SerializedName("EMAIL")
    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("INDUSTRY_TYPE_ID")
    public String getIndustryTypeId() {
        return industryTypeId;
    }

    @SerializedName("INDUSTRY_TYPE_ID")
    public void setIndustryTypeId(String industryTypeId) {
        this.industryTypeId = industryTypeId;
    }

    @SerializedName("MID")
    public String getmId() {
        return mId;
    }

    @SerializedName("MID")
    public void setmId(String mId) {
        this.mId = mId;
    }

    @SerializedName("MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    @SerializedName("MOBILE_NO")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @SerializedName("ORDER_ID")
    public String getOrderId() {
        return orderId;
    }

    @SerializedName("ORDER_ID")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @SerializedName("REQUEST_TYPE")
    public String getRequestType() {
        return requestType;
    }

    @SerializedName("REQUEST_TYPE")
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @SerializedName("TXN_AMOUNT")
    public int getTxnAmount() {
        return txnAmount;
    }

    @SerializedName("TXN_AMOUNT")
    public void setTxnAmount(int txnAmount) {
        this.txnAmount = txnAmount;
    }

    @SerializedName("WEBSITE")
    public String getWebsite() {
        return website;
    }

    @SerializedName("WEBSITE")
    public void setWebsite(String website) {
        this.website = website;
    }

    @SerializedName("CHECKSUMHASH")
    public String getCheckSumHash() {
        return checkSumHash;
    }

    @SerializedName("CHECKSUMHASH")
    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }

    @SerializedName("joined_challenge_info")
    public JoinedChallengeInfo getJoinedChallengeInfo() {
        return joinedChallengeInfo;
    }

    @SerializedName("joined_challenge_info")
    public void setJoinedChallengeInfo(JoinedChallengeInfo joinedChallengeInfo) {
        this.joinedChallengeInfo = joinedChallengeInfo;
    }

}
