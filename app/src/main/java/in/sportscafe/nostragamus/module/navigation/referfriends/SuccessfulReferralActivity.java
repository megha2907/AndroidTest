package in.sportscafe.nostragamus.module.navigation.referfriends;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.WordUtils;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;

/**
 * Created by deepanshi on 6/30/17.
 */

public class SuccessfulReferralActivity extends NostragamusActivity implements View.OnClickListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.SUCCESSFUL_REFERRAL;
    }

    private TextView tvSuccessfulReferralOne;
    private TextView tvSuccessfulReferralTwo;
    private TextView tvSuccessfulReferralThree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_referral);
        initialize();
        NostragamusAnalytics.getInstance().trackReferralBenefitScreenShown();
    }

    private void initialize() {
        tvSuccessfulReferralOne = (TextView) findViewById(R.id.successful_referral_text_one);
        tvSuccessfulReferralTwo = (TextView) findViewById(R.id.successful_referral_text_two);
        findViewById(R.id.start_playing_btn).setOnClickListener(this);
        setSuccessfulReferralInfo();
    }

    private void setSuccessfulReferralInfo() {

        if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getUserReferralName())) {
            tvSuccessfulReferralOne.setText("Welcome to Nostragamus! Here's a gift from us and "
                    + WordUtils.capitalize(NostragamusDataHandler.getInstance().getUserReferralName()) + " to start you off!");
        } else {
            tvSuccessfulReferralOne.setText("Welcome to Nostragamus! Here's a gift from us to start you off!");
        }

        if (NostragamusDataHandler.getInstance().getWalletInitialAmount() != null) {

            tvSuccessfulReferralTwo.setVisibility(View.VISIBLE);
            if (NostragamusDataHandler.getInstance().getWalletInitialAmount() > 0) {
                tvSuccessfulReferralTwo.setText(WalletHelper.getFormattedStringOfAmount
                        (NostragamusDataHandler.getInstance().getWalletInitialAmount()) +
                        " has been added to your wallet");
            }
        } else {
            tvSuccessfulReferralTwo.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_playing_btn:
                onStartPlayingClicked();
                break;
        }
    }

    private void onStartPlayingClicked() {
      navigateToHome();
    }

    public void navigateToHome() {
        Intent intent = new Intent(this, NostraHomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.LOGIN_SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


}
