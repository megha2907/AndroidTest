package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm;

import android.os.Bundle;

/**
 * Created by sc on 28/11/17.
 */

public interface AddMoneyThroughPaytmFragmentListener {
    void onPaytmMoneyAddSuccess();
    void launchPaymentCouponFragment(Bundle args);
}
