package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto;

/**
 * Created by sandip on 09/06/17.
 */

public class PayoutChoiceDto {

    private boolean isSelected;
    private String accountName;
    private String accountNumber;

    public PayoutChoiceDto() {
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
