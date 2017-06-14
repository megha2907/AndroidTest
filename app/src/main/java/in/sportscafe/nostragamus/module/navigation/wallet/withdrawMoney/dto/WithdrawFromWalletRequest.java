package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by sandip on 13/06/17.
 */
@Parcel
public class WithdrawFromWalletRequest {

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("payout")
    private String payoutChoice;

    @JsonProperty("amount")
    public double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @JsonProperty("payout")
    public String getPayoutChoice() {
        return payoutChoice;
    }

    @JsonProperty("payout")
    public void setPayoutChoice(String payoutChoice) {
        this.payoutChoice = payoutChoice;
    }
}
