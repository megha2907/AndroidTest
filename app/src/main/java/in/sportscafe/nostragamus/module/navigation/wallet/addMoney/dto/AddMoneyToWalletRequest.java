package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by sandip on 07/06/17.
 */
@Parcel
public class AddMoneyToWalletRequest {

    @JsonProperty("txn_amount")
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
