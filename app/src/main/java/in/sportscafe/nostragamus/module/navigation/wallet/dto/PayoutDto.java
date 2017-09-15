package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 12/06/17.
 */

public class PayoutDto {

    @SerializedName("paytm")
    private List<WalletPaytmDto> paytm = null;

    @SerializedName("bank")
    private List<WalletBankDto> bank = null;

    @SerializedName("paytm")
    public List<WalletPaytmDto> getPaytm() {
        return paytm;
    }

    @SerializedName("paytm")
    public void setPaytm(List<WalletPaytmDto> paytm) {
        this.paytm = paytm;
    }

    @SerializedName("bank")
    public List<WalletBankDto> getBank() {
        return bank;
    }

    @SerializedName("bank")
    public void setBank(List<WalletBankDto> bank) {
        this.bank = bank;
    }

}
