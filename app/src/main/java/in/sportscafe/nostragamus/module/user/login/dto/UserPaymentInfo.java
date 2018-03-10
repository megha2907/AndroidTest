package in.sportscafe.nostragamus.module.user.login.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 12/04/17.
 */
@Parcel
public class UserPaymentInfo {

    @SerializedName("cashfree")
    private UserPaymentInfoBankDto bank;

    @SerializedName("paytm")
    private UserPaymentInfoPaytmDto paytm;

    @SerializedName("cashfree")
    public UserPaymentInfoBankDto getBank() {
        return bank;
    }

    @SerializedName("cashfree")
    public void setBank(UserPaymentInfoBankDto bank) {
        this.bank = bank;
    }

    @SerializedName("paytm")
    public UserPaymentInfoPaytmDto getPaytm() {
        return paytm;
    }

    @SerializedName("paytm")
    public void setPaytm(UserPaymentInfoPaytmDto paytm) {
        this.paytm = paytm;
    }
}
