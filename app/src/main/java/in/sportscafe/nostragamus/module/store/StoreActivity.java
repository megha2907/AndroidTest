package in.sportscafe.nostragamus.module.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.join.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 7/25/17.
 */

public class StoreActivity extends NostragamusActivity implements StoreFragmentListener {

    private static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 502;

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.STORE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        initialize();
        loadStoreFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.store_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.store_toolbar_tv);
        tvToolbar.setText("Shop - Buy Powerups");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void loadStoreFragment() {
        StoreFragment fragment = new StoreFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            fragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onLowBalanceWhileBuy(Bundle args) {
        Intent intent = new Intent(getActivity(), AddMoneyOnLowBalanceActivity.class);
        if (args != null) {
            intent.putExtras(args);
        }
        startActivityForResult(intent, ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.getExtras() != null) {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof StoreFragment) {
                ((StoreFragment) fragment).fetchUserWalletFromServer(CompletePaymentDialogFragment.DialogLaunchMode.STORE_BUY_POWERUP_AFTER_LOW_BAL_LAUNCH,
                        data.getExtras());
            }
        }
    }
}
