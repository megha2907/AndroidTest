package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance;

import android.os.Bundle;

/**
 * Created by sandip on 16/06/17.
 */

public interface AddMoneyOnLowBalanceFragmentListener {
    void onBackClicked();
    void onSuccess(Bundle args);
}
