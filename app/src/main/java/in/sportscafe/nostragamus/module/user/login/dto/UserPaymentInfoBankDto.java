package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/04/17.
 */
@Parcel
public class UserPaymentInfoBankDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("ifsc_code")
    private String ifscCode;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ifsc_code")
    public String getIfscCode() {
        return ifscCode;
    }

    @JsonProperty("ifsc_code")
    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @JsonProperty("account_no")
    public String getAccountNo() {
        return accountNo;
    }

    @JsonProperty("account_no")
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
