package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by sandip on 12/06/17.
 */

public class PayoutDto {

    @JsonProperty("paytm")
    private List<WalletPaytmDto> paytm = null;

    @JsonProperty("bank")
    private List<WalletBankDto> bank = null;

    @JsonProperty("paytm")
    public List<WalletPaytmDto> getPaytm() {
        return paytm;
    }

    @JsonProperty("paytm")
    public void setPaytm(List<WalletPaytmDto> paytm) {
        this.paytm = paytm;
    }

    @JsonProperty("bank")
    public List<WalletBankDto> getBank() {
        return bank;
    }

    @JsonProperty("bank")
    public void setBank(List<WalletBankDto> bank) {
        this.bank = bank;
    }

}
