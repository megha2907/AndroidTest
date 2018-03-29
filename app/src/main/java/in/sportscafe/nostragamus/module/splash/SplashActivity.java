package in.sportscafe.nostragamus.module.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.getstart.GetStartActivity;
import in.sportscafe.nostragamus.module.getstart.Splash;
import in.sportscafe.nostragamus.module.privateContest.ui.branchShare.ShareDetailsDto;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // To get the updated app settings like version details
        Nostragamus.getInstance().startPeriodJobs();
        NostragamusAnalytics.getInstance().trackAppOpening(AnalyticsLabels.LAUNCHER);

    }

    @Override
    protected void onStart() {
        super.onStart();


        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {

                /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
                 * Launch Monster viewer activity if a link clicked without $android_deeplink_path
                 */
                if (error == null) {

                    Log.d("BranchParams:--", referringParams.toString());
                    Log.d("BranchLastParams:--", Branch.getInstance().getLatestReferringParams().toString());
                    Log.d("BranchFirstParams:--", Branch.getInstance().getFirstReferringParams().toString());

                    NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();

                    try {

                        if (referringParams.has(BundleKeys.USER_REFERRAL_CODE)) {
                            nostragamusDataHandler.setUserReferralCode(referringParams.getString(BundleKeys.USER_REFERRAL_CODE));
                        }

                        if (referringParams.has(BundleKeys.USER_REFERRAL_PHOTO)) {
                            nostragamusDataHandler.setUserReferralPhoto(referringParams.getString(BundleKeys.USER_REFERRAL_PHOTO));
                        }

                        if (referringParams.has(BundleKeys.USER_REFERRAL_NAME)) {
                            nostragamusDataHandler.setUserReferralName(referringParams.getString(BundleKeys.USER_REFERRAL_NAME));
                        }

                        if (referringParams.has(BundleKeys.WALLET_INITIAL_AMOUNT)) {
                            nostragamusDataHandler.setWalletInitialAmount(Integer.parseInt(referringParams.getString(BundleKeys.WALLET_INITIAL_AMOUNT)));
                        }

                        if (referringParams.has(BundleKeys.LINK_NAME)) {
                            nostragamusDataHandler.setInstallLinkName(referringParams.getString(BundleKeys.LINK_NAME));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("BranchValue:--", "No referral Code / No Photo / wallet init / referral name");
                    }

                    try {
                        if (referringParams.has("~channel")) {
                            nostragamusDataHandler.setInstallChannel(referringParams.getString("~channel"));
                        }

                        if (referringParams.has("~campaign")) {
                            nostragamusDataHandler.setInstallReferralCampaign(referringParams.getString("~campaign"));
                            if (!referringParams.getString("~campaign").equalsIgnoreCase("App Invite") ||
                                    !referringParams.getString("~campaign").equalsIgnoreCase("Group Invite")) {
                                NostragamusDataHandler.getInstance().setMarketingCampaign(true);
                            }
                        }

                        if (referringParams.has("~campaign")) {
                            checkForPrivateContest(referringParams);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("BranchValue:--", "No Campaign / No Channel");
                    }
                } else {
                    Log.d("BranchError:--", error.getMessage());
                }
                navigateToGetStarted();
            }
        }, this.getIntent().getData(), this);

    }

    private void checkForPrivateContest(JSONObject referringParams) {
        try {
            if (referringParams.getString("~campaign").equalsIgnoreCase(Constants.PrivateContests.BranchLink.LINKED_PROPERTIES_CAMPAIGN) &&
                    referringParams.has(Constants.PrivateContests.BranchLink.PRIVATE_CONTEST_INVITATION_CODE)) {

                String privateContestInvitationCode = referringParams.getString(Constants.PrivateContests.BranchLink.PRIVATE_CONTEST_INVITATION_CODE);
                String userNick = referringParams.getString(Constants.PrivateContests.BranchLink.USER_NICK);
                String userPhotoUrl = referringParams.getString(Constants.PrivateContests.BranchLink.USER_PHOTO_URL);

                Log.d(TAG, String.format("Private Contest link opened! \n code : %s, UserNick : %s, UserPhoto : %s ",
                        privateContestInvitationCode, userNick, userPhotoUrl));

                // Save into data handler
                ShareDetailsDto shareDetailsDto = new ShareDetailsDto();
                shareDetailsDto.setPrivateCode(privateContestInvitationCode);
                shareDetailsDto.setUserNick(userNick);
                shareDetailsDto.setUserPhotoUrl(userPhotoUrl);

                String string = new Gson().toJson(shareDetailsDto);

                NostragamusDataHandler.getInstance().setPrivateContestInvitationCode(string);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
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