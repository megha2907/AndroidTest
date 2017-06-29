package in.sportscafe.nostragamus.module.navigation.referfriends;

/**
 * Created by deepanshi on 6/21/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jeeva.android.ExceptionTracker;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralCreditActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class ReferFriendActivity extends NostragamusActivity implements ReferFriendFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.REFER_FRIEND;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);

        initialize();
        loadReferFriendFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.refer_friend_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.refer_friend_toolbar_tv);
        tvToolbar.setText("Refer a Friend and earn cash");
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
        ReferFriendFragment fragment = new ReferFriendFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onReferralCreditsClicked(Bundle bundle) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), ReferralCreditActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onTermsClicked() {

    }

    @Override
    public void onReferAFriendClicked(String referralCode,String walletInit) {
        navigateToReferFriend(referralCode,walletInit);
    }

    private void navigateToReferFriend(String referralCode, String walletInit) {
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
                            String shareText = "I’m playing Nostragamus, the sports prediction app. Join and lets see who does better! Click this link to download the app " + url;
                            AppSnippet.doGeneralShare(getApplicationContext(), shareText);
                        } else {
                            ExceptionTracker.track(error.getMessage());
                        }
                    }
                });
    }
}