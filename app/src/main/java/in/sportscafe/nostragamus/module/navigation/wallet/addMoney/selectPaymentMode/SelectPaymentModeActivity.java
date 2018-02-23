package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddWalletMoneyFragment;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 2/23/18.
 */

public class SelectPaymentModeActivity extends NostragamusActivity implements SelectPaymentModeFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.SELECT_PAYMENT_MODE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_select_payment_mode);

        loadSelectPaymentModeFragment();
    }

    private void loadSelectPaymentModeFragment() {
        SelectPaymentModeFragment fragment = new SelectPaymentModeFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof SelectPaymentModeFragment) {
            ((SelectPaymentModeFragment) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onSuccess() {
        /*Intent intent = new Intent(this, WalletHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/

        /* Once money added , remove this activity */
        super.onBackPressed();

    }
}
