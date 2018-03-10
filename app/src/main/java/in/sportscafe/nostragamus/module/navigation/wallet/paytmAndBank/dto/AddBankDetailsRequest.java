package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 10/04/17.
 */

public class AddBankDetailsRequest {

    @SerializedName("payment_mode")
    private String paymentMode;

    @SerializedName("name")
    private String name;

    @SerializedName("ifsc")
    private String ifscCode;

    @SerializedName("bankAccount")
    private String accountNo;

    @SerializedName("address1")
    private String addressLineOne;

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }
}
