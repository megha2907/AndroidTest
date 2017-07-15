package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jeeva.android.ExceptionTracker;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionSuccessDialogFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;
import in.sportscafe.nostragamus.utils.FragmentHelper;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

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
        if (BuildConfig.IS_PAID_VERSION) {
            tvToolbar.setText("Referral Credits");
        }else {
            tvToolbar.setText("My Friends");
        }
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
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }
        ReferralCreditFragment referralCreditFragment = new ReferralCreditFragment();
        referralCreditFragment.setArguments(args);
        FragmentHelper.replaceFragment(this, R.id.fragment_container, referralCreditFragment);
    }

    @Override
    public void onPowerUpRewardsClicked() {

    }

    @Override
    public void onCashRewardsClicked() {

    }

    @Override
    public void onReferAFriendClicked(String referralCode, String walletInit) {
        navigateToReferFriend(referralCode,walletInit);
    }

    private void navigateToReferFriend(final String referralCode, final String walletInit) {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(Constants.BundleKeys.WALLET_INITIAL_AMOUNT, walletInit)
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_CODE, referralCode)
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_PHOTO, NostragamusDataHandler.getInstance().getUserInfo().getPhoto())
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_NAME, NostragamusDataHandler.getInstance().getUserInfo().getUserName());

        LinkProperties linkProperties = new LinkProperties()
                .addTag("inviteApp")
                .setFeature("inviteApp")
                .setChannel("App")
                .setCampaign("App Normal Invite")
                .addControlParameter("$android_deeplink_path", "app/invite/");

        buo.generateShortUrl(getContext(), linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (null == error) {
                            String shareText = "If you love sports, try out India's first live sports predictions game and win REAL money." +
                                    "Here's ₹ " + walletInit +" for you to enter a contest - just download the app using this link " + url +
                                    "  or use my referral code "+ referralCode + " at login."; AppSnippet.doGeneralShare(getApplicationContext(), shareText);
                        } else {
                            ExceptionTracker.track(error.getMessage());
                        }
                    }
                });
    }

}