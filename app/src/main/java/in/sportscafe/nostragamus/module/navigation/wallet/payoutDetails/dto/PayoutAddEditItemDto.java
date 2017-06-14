package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 08/06/17.
 */
@Parcel
public class PayoutAddEditItemDto {

    private int viewType;
    private String accountName;
    private String accountNumber;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
