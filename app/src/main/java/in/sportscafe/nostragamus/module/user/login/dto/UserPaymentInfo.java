package in.sportscafe.nostragamus.module.user.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/04/17.
 */
@Parcel
public class UserPaymentInfo {

    @JsonProperty("bank")
    private UserPaymentInfoBankDto bank;

    @JsonProperty("paytm")
    private UserPaymentInfoPaytmDto paytm;

    @JsonProperty("bank")
    public UserPaymentInfoBankDto getBank() {
        return bank;
    }

    @JsonProperty("bank")
    public void setBank(UserPaymentInfoBankDto bank) {
        this.bank = bank;
    }

    @JsonProperty("paytm")
    public UserPaymentInfoPaytmDto getPaytm() {
        return paytm;
    }

    @JsonProperty("paytm")
    public void setPaytm(UserPaymentInfoPaytmDto paytm) {
        this.paytm = paytm;
    }
}
