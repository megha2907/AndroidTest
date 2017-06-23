package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;

/**
 * Created by sandip on 12/04/17.
 */

public interface AddBankDetailModel {
    void savePaymentBankDetails(String accountHolderName, String accNumber, String ifsCode);
}
