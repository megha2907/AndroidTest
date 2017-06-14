package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PayoutWalletChoiceActivity extends NostragamusActivity implements PayoutWalletChoiceFragmentListener {

    private static final int ADD_PAYOUT_REQUEST_CODE = 21;

    @Override
    public String getScreenName() {
        return null; //Constants.ScreenNames.WALLET_PAYOUT_CHOICE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_wallet_choice);

        initToolbar();
        loadChoiceFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.payout_choice_toolbar);
        toolbar.setTitle("Choose Account");
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

    private void loadChoiceFragment() {
        PayoutWalletChoiceFragment fragment = new PayoutWalletChoiceFragment();
        if (getIntent() != null) {
            fragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onAddPayoutDetailsClicked() {
        Intent intent = new Intent(this, PayoutWalletHomeActivity.class);
        startActivityForResult(intent, ADD_PAYOUT_REQUEST_CODE);
    }

    @Override
    public void onWithdrawSuccessful() {
        Intent intent = new Intent(this, WalletHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PAYOUT_REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof PayoutWalletChoiceFragment) {
                ((PayoutWalletChoiceFragment) fragment).onPayoutDetailsAdded();
            }
        }
    }
}
