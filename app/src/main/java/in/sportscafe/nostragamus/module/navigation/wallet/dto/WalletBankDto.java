package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 12/06/17.
 */

public class WalletBankDto {

    @JsonProperty("holderName")
    private String holderName;

    @JsonProperty("accNumber")
    private String accNumber;

    @JsonProperty("ifsc")
    private String ifsc;

    @JsonProperty("holderName")
    public String getHolderName() {
        return holderName;
    }

    @JsonProperty("holderName")
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    @JsonProperty("accNumber")
    public String getAccNumber() {
        return accNumber;
    }

    @JsonProperty("accNumber")
    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    @JsonProperty("ifsc")
    public String getIfsc() {
        return ifsc;
    }

    @JsonProperty("ifsc")
    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

}
