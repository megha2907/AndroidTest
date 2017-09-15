package in.sportscafe.nostragamus.module.popups.bankpopups;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 02/03/17.
 */
public class BankTransferResponse {

    @SerializedName("data")
    private BankTransfer bankTransfer;

    @SerializedName("data")
    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

    @SerializedName("data")
    public void setBankTransfer(BankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;
    }
}