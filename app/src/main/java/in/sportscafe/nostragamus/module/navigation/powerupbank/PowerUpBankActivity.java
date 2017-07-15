package in.sportscafe.nostragamus.module.navigation.powerupbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction.PBTransactionActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 7/12/17.
 */

public class PowerUpBankActivity extends NostragamusActivity implements PowerUpBankFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.POWERUP_BANK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerup_bank);

        initialize();
        loadReferFriendFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.powerup_bank_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.powerup_bank_toolbar_tv);
        tvToolbar.setText("Powerups Bank");

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

    private void loadReferFriendFragment() {
        PowerUpBankFragment fragment = new PowerUpBankFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onEarnMorePowerUpsClicked() {
        if (getActivity() != null) {
//            Intent intent = new Intent(getActivity(), ReferralCreditActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void onTermsClicked() {
        navigateToWebView(Constants.WebPageUrls.REFERRAL_TERMS, "Terms and Conditions");
    }

    @Override
    public void onPowerUpTransactionHistoryClicked(Bundle bundle) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), PBTransactionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
