package in.sportscafe.nostragamus.module.paytm;

/**
 * Created by sandip on 12/04/17.
 */

public interface AddPaymentBankDetailModel {
    void savePaymentBankDetails(String accountHolderName, String accNumber, String ifsCode);
}
