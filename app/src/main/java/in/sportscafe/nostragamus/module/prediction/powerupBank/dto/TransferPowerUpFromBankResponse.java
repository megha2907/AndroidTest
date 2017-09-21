package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;



/**
 * Created by sandip on 21/09/17.
 */

public class TransferPowerUpFromBankResponse {

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
