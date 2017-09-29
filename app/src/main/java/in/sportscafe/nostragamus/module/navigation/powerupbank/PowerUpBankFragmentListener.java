package in.sportscafe.nostragamus.module.navigation.powerupbank;

import android.os.Bundle;

/**
 * Created by deepanshi on 7/12/17.
 */

public interface PowerUpBankFragmentListener {

    void onEarnMorePowerUpsClicked();

    void onTermsClicked();

    void onPowerUpTransactionHistoryClicked(Bundle bundle);

    void onStoreClicked();
}
