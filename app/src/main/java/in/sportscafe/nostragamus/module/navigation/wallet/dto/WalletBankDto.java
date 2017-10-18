package in.sportscafe.nostragamus.module.navigation.wallet.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 12/06/17.
 */

public class WalletBankDto {

    @SerializedName("holderName")
    private String holderName;

    @SerializedName("accNumber")
    private String accNumber;

    @SerializedName("ifsc")
    private String ifsc;

    @SerializedName("holderName")
    public String getHolderName() {
        return holderName;
    }

    @SerializedName("holderName")
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    @SerializedName("accNumber")
    public String getAccNumber() {
        return accNumber;
    }

    @SerializedName("accNumber")
    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    @SerializedName("ifsc")
    public String getIfsc() {
        return ifsc;
    }

    @SerializedName("ifsc")
    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

}
