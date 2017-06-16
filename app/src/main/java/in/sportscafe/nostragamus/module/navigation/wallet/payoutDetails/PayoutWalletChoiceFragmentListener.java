package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.os.Bundle;

/**
 * Created by sandip on 09/06/17.
 */

public interface PayoutWalletChoiceFragmentListener {
    void onAddPayoutDetailsClicked();
    void onWithdrawSuccessful(Bundle args);
    void onWithdrawFailure(Bundle args);
}
