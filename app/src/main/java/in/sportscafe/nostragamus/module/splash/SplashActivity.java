package in.sportscafe.nostragamus.module.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.getstart.GetStartActivity;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // To get the updated app settings like version details
        Nostragamus.getInstance().startPeriodJobs();

        NostragamusAnalytics.getInstance().trackAppOpening(AnalyticsLabels.LAUNCHER);
        NostragamusAnalytics.getInstance().setUserProperties();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch.getInstance().initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError branchError) {

                /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
                 * Launch Monster viewer activity if a link clicked without $android_deeplink_path
                 */
                if (null != branchUniversalObject) {
                    NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();
                    HashMap<String, String> metadata = branchUniversalObject.getMetadata();

                    if (metadata.containsKey(BundleKeys.USER_REFERRAL_ID)) {
                        nostragamusDataHandler.setReferralUserId(metadata.get(BundleKeys.USER_REFERRAL_ID));
                    }

                    if (null != linkProperties) {
                        Log.d("Install Channel", linkProperties.getChannel() + "");
                        nostragamusDataHandler.setInstallChannel(linkProperties.getChannel());
                    }

                    String path = metadata.get("$android_deeplink_path");
                    if (null != path) {
                        if (path.equalsIgnoreCase("group/invite/")) {
                            if (nostragamusDataHandler.isLoggedInUser()) {
                                navigateToJoinGroup(metadata.get(BundleKeys.GROUP_CODE));
                                return;
                            }
                            
                            nostragamusDataHandler.setInstallGroupCode(metadata.get(BundleKeys.GROUP_CODE));
                            nostragamusDataHandler.setInstallGroupName(metadata.get(BundleKeys.GROUP_NAME));
                        }

                        if (path.equalsIgnoreCase("app/invite/")) {
                            navigateToGetStarted();
                            return;
                        }
                    }
                }

                navigateToGetStarted();
            }
        }, this.getIntent().getData(), this);
    }

    private void navigateToGetStarted() {
        startActivity(new Intent(this, GetStartActivity.class));
        finish();
    }

    private void navigateToJoinGroup(String groupCode) {
        Log.d("SplashActivity", "Join Group start called");
        Intent intent = new Intent(this, JoinGroupActivity.class);
        intent.putExtra(BundleKeys.GROUP_CODE, groupCode);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}