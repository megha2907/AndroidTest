package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/04/17.
 */
@Parcel
public class UserPaymentInfoBankDto {

    @SerializedName("name")
    private String name;

    @SerializedName("address1")
    private String address1;

    @SerializedName("ifsc")
    private String ifscCode;

    @SerializedName("bankAccount")
    private String accountNo;

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("ifsc_code")
    public String getIfscCode() {
        return ifscCode;
    }

    @SerializedName("ifsc_code")
    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @SerializedName("account_no")
    public String getAccountNo() {
        return accountNo;
    }

    @SerializedName("account_no")
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }
}
