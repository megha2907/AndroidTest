package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 7/4/17.
 */

public class GenerateCheckSumRequest {

    @JsonProperty("REQUEST_TYPE")
    private String requestType;

    @JsonProperty("MID")
    private String mID;

    @JsonProperty("CUST_ID")
    private String cUSTID;

    @JsonProperty("INDUSTRY_TYPE_ID")
    private String iNDUSTRYTYPEID;

    @JsonProperty("CHANNEL_ID")
    private String cHANNELID;

    @JsonProperty("TXN_AMOUNT")
    private String tXNAMOUNT;

    @JsonProperty("WEBSITE")
    private String wEBSITE;

    @JsonProperty("CALLBACK_URL")
    private String cALLBACKURL;

    @JsonProperty("EMAIL")
    private String eMAIL;

    @JsonProperty("MOBILE_NO")
    private String mOBILENO;

    @JsonProperty("ORDER_ID")
    private String oRDERID;


    @JsonProperty("REQUEST_TYPE")
    public String getRequestType() {
        return requestType;
    }

    @JsonProperty("REQUEST_TYPE")
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @JsonProperty("MID")
    public String getMID() {
        return mID;
    }

    @JsonProperty("MID")
    public void setMID(String mID) {
        this.mID = mID;
    }

    @JsonProperty("CUST_ID")
    public String getCUSTID() {
        return cUSTID;
    }

    @JsonProperty("CUST_ID")
    public void setCUSTID(String cUSTID) {
        this.cUSTID = cUSTID;
    }

    @JsonProperty("INDUSTRY_TYPE_ID")
    public String getINDUSTRYTYPEID() {
        return iNDUSTRYTYPEID;
    }

    @JsonProperty("INDUSTRY_TYPE_ID")
    public void setINDUSTRYTYPEID(String iNDUSTRYTYPEID) {
        this.iNDUSTRYTYPEID = iNDUSTRYTYPEID;
    }

    @JsonProperty("CHANNEL_ID")
    public String getCHANNELID() {
        return cHANNELID;
    }

    @JsonProperty("CHANNEL_ID")
    public void setCHANNELID(String cHANNELID) {
        this.cHANNELID = cHANNELID;
    }

    @JsonProperty("TXN_AMOUNT")
    public String getTXNAMOUNT() {
        return tXNAMOUNT;
    }

    @JsonProperty("TXN_AMOUNT")
    public void setTXNAMOUNT(String tXNAMOUNT) {
        this.tXNAMOUNT = tXNAMOUNT;
    }

    @JsonProperty("WEBSITE")
    public String getWEBSITE() {
        return wEBSITE;
    }

    @JsonProperty("WEBSITE")
    public void setWEBSITE(String wEBSITE) {
        this.wEBSITE = wEBSITE;
    }

    @JsonProperty("CALLBACK_URL")
    public String getCALLBACKURL() {
        return cALLBACKURL;
    }

    @JsonProperty("CALLBACK_URL")
    public void setCALLBACKURL(String cALLBACKURL) {
        this.cALLBACKURL = cALLBACKURL;
    }

    @JsonProperty("EMAIL")
    public String getEMAIL() {
        return eMAIL;
    }

    @JsonProperty("EMAIL")
    public void setEMAIL(String eMAIL) {
        this.eMAIL = eMAIL;
    }

    @JsonProperty("MOBILE_NO")
    public String getMOBILENO() {
        return mOBILENO;
    }

    @JsonProperty("MOBILE_NO")
    public void setMOBILENO(String mOBILENO) {
        this.mOBILENO = mOBILENO;
    }

    @JsonProperty("ORDER_ID")
    public String getoRDERID() {
        return oRDERID;
    }

    @JsonProperty("ORDER_ID")
    public void setoRDERID(String oRDERID) {
        this.oRDERID = oRDERID;
    }
}
