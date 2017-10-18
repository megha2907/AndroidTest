package in.sportscafe.nostragamus.module.prediction.powerupBank;

import android.os.Bundle;

/**
 * Created by sandip on 21/07/17.
 */

public interface PowerUpBankTransferFragmentListener {
    void updatePowerUpInfoDetails(Bundle args);
    void finishActivity(Bundle args);
    void launchStore(Bundle args);
}
