package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendFragment;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 6/23/17.
 */

public class ReferralCreditActivity extends NostragamusActivity implements ReferralCreditFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.REFERRAL_CREDIT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_credits);

        initialize();
        loadReferralCreditFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.referral_credits_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.referral_credits_toolbar_tv);
        tvToolbar.setText("Referral Credits");
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

    private void loadReferralCreditFragment() {
        ReferralCreditFragment fragment = new ReferralCreditFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onPowerUpRewardsClicked() {

    }

    @Override
    public void onCashRewardsClicked() {

    }
}