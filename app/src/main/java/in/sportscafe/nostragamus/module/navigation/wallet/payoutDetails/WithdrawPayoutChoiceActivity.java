package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.WithdrawApiDialogListener;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.WithdrawInitiatedDialogFragment;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class WithdrawPayoutChoiceActivity extends NostragamusActivity implements WithdrawPayoutChoiceFragmentListener {

    private static final int ADD_PAYOUT_REQUEST_CODE = 21;

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.WALLET_PAYOUT_CHOICE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_payout_choice);

        initToolbar();
        loadChoiceFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.payout_choice_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
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

    private void loadChoiceFragment() {
        WithdrawPayoutChoiceFragment fragment = new WithdrawPayoutChoiceFragment();
        if (getIntent() != null) {
            fragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onAddPayoutDetailsClicked() {
        Bundle args = new Bundle();
        args.putInt(Constants.BundleKeys.LAUNCH_MODE, PayoutLaunchMode.FROM_WITHDRAWAL_AS_NO_PAY_OUT_ACC);

        Intent intent = new Intent(this, PayoutWalletHomeActivity.class);
        intent.putExtras(args);
        startActivityForResult(intent, ADD_PAYOUT_REQUEST_CODE);
    }

    @Override
    public void onWithdrawSuccessful(Bundle args) {
        showConfirmationDialog(args);
    }

    @Override
    public void onWithdrawFailure(Bundle args) {
        showConfirmationDialog(args);
    }

    private void showConfirmationDialog(Bundle args) {
        int requestCode = 1390;
        if (args == null) {
            args = new Bundle();
        }
        args.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);

        WithdrawInitiatedDialogFragment dialogFragment =
                WithdrawInitiatedDialogFragment.newInstance(getDialogListener());
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "WITHDRAW_DIALOG");
    }

    private WithdrawApiDialogListener getDialogListener() {
        return new WithdrawApiDialogListener() {
            @Override
            public void onOkClicked() {
                gotoWalletHome();
            }
        };
    }

    private void gotoWalletHome() {
        Intent intent = new Intent(this, WalletHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PAYOUT_REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof WithdrawPayoutChoiceFragment) {
                ((WithdrawPayoutChoiceFragment) fragment).onPayoutDetailsAdded();
            }
        }
    }
}
