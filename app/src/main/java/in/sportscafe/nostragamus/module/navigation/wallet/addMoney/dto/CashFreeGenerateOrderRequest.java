package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 2/23/18.
 */

public class CashFreeGenerateOrderRequest {

    @SerializedName("txn_amount")
    private double amount;

    @SerializedName("type")
    private String transactionType;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

}
