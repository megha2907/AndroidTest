package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;


/**
 * Created by sandip on 21/09/17.
 */

public class TransferPowerUpFromBankResponse {

    @SerializedName("data")
    private TransferPowerUpFromBankResponseData data;

    public TransferPowerUpFromBankResponseData getData() {
        return data;
    }

    public void setData(TransferPowerUpFromBankResponseData data) {
        this.data = data;
    }
}
