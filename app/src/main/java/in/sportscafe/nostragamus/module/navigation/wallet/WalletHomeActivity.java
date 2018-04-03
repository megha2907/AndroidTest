package in.sportscafe.nostragamus.module.navigation.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddWalletMoneyActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.doKYC.AddKYCDetailsActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.doKYC.KYCVerificationInProgressPopup;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.PayoutWalletHomeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.WithdrawWalletMoneyActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class WalletHomeActivity extends NostragamusActivity implements WalletHomeFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_WALLET_HOME;
    }

    public static final int ADD_KYC_ACTIVITY_REQUEST_CODE = 1516;

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

    private void notifyWalletHomeFragmentAndUpdateKYCStatus() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null &&  fragment instanceof WalletHomeFragment) {
            ((WalletHomeFragment) fragment).refreshKYCStatus();
        }
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.wallet_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.wallet_toolbar_tv);
        tvToolbar.setText("Wallet");
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

    @Override
    public void onKYCClicked() {
        Intent intent = new Intent(this, AddKYCDetailsActivity.class);
        startActivityForResult(intent, ADD_KYC_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onOpenKYCRequiredPopup() {
        Intent intent = new Intent(this, KYCVerificationInProgressPopup.class);
        startActivity(intent);
    }

    @Override
    public void onOpenKYCBlockedPopup() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_KYC_ACTIVITY_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    // KYC Uploaded, re create screen as kyc status needs to be updated
                    notifyWalletHomeFragmentAndUpdateKYCStatus();
                    showSnackbarMessage(R.drawable.edit_answer_success_snackbar_icn, "Your KYC details were uploaded successfully!");
                    break;
            }
        }
    }

    private void showSnackbarMessage(int imgResource, String msg) {
        final CustomSnackBar snackBar = CustomSnackBar.make(findViewById(R.id.wallet_home_root_layout), msg, CustomSnackBar.DURATION_SECS_5);

        if (imgResource > 0) {
            snackBar.setImageResource(imgResource);
        }
        snackBar.setAction("GOT IT", null);
        snackBar.show();
    }

}
