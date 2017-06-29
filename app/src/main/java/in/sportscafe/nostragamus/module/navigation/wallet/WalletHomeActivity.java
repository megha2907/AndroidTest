package in.sportscafe.nostragamus.module.navigation.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddWalletMoneyActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.PayoutWalletHomeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.WithdrawWalletMoneyActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class WalletHomeActivity extends NostragamusActivity implements WalletHomeFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.WALLET_HOME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_home);

        initToolbar();
        loadWalletFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        notifyWalletHomeFragment();
    }

    private void notifyWalletHomeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null &&  fragment instanceof WalletHomeFragment) {
            ((WalletHomeFragment) fragment).refreshWalletDetails();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.wallet_toolbar);
        toolbar.setTitle("Wallet");
        setSupportActionBar(toolbar);

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

    private void loadWalletFragment() {
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }

        WalletHomeFragment walletHomeFragment = new WalletHomeFragment();
        walletHomeFragment.setArguments(args);
        FragmentHelper.replaceFragment(this, R.id.fragment_container, walletHomeFragment);
    }

    @Override
    public void onEarnMoreClicked() {
        Intent intent = new Intent(this, ReferFriendActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAddMoneyClicked() {
        Intent intent = new Intent(this, AddWalletMoneyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWithdrawMoneyClicked() {
        Intent intent = new Intent(this, WithdrawWalletMoneyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTransactionHistoryClicked() {
        Intent intent = new Intent(this, WalletHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPayoutDetailsClicked() {
        Intent intent = new Intent(this, PayoutWalletHomeActivity.class);
        startActivity(intent);
    }
}
