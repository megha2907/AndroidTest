package in.sportscafe.nostragamus.module.popups.bankpopups;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 02/03/17.
 */
public class BankTransferResponse {

    @JsonProperty("data")
    private BankTransfer bankTransfer;

    @JsonProperty("data")
    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

    @JsonProperty("data")
    public void setBankTransfer(BankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;
    }
}