package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 13/06/17.
 */
@Parcel
public class WithdrawFromWalletRequest {

    @SerializedName("amount")
    private double amount;

    @SerializedName("payout")
    private String payoutChoice;

    @SerializedName("amount")
    public double getAmount() {
        return amount;
    }

    @SerializedName("amount")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @SerializedName("payout")
    public String getPayoutChoice() {
        return payoutChoice;
    }

    @SerializedName("payout")
    public void setPayoutChoice(String payoutChoice) {
        this.payoutChoice = payoutChoice;
    }
}
