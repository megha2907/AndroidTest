package in.sportscafe.nostragamus.module.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.AllChallengesFragment;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeInfoDialogFragment;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.paytm.WalletOrBankConnectActivity;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingFragment;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileFragment;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends NostragamusActivity implements OnHomeActionListener ,OnDismissListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private static final int CHALLENGE_JOINED_INFO_REQUEST_CODE = 42;
    private static final int JOIN_CHALLENGE_REQUEST_CODE = 43;

    private int mProfileTabPosition = 0;

    private View mSelectedImage;

    private View mSelectedText;

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (null == NostragamusDataHandler.getInstance().getUserId()) {
            navigateToLogIn();
            return;
        }

        UserInfoModelImpl.newInstance(getUserInfoCallBackListener()).getUserInfo();

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            if (bundle.containsKey(BundleKeys.OPEN_PROFILE)) {
                mProfileTabPosition = Integer.parseInt(bundle.getString(BundleKeys.OPEN_PROFILE));
                showProfile();
                return;
            } else if (bundle.containsKey(BundleKeys.GROUP)) {
                showGroups();
                return;
            }
        }
        showFirstTab();
    }

    /**
     * If user has not yet provided payment info (either paytm or bank) then he'll be asked to provide
     * @param userInfo
     */
    private void checkPaymentInfoProvidedOrRequired(UserInfo userInfo) {
        if (userInfo != null) {
            if (userInfo.getUserPaymentInfo() == null) {

                Log.d(TAG, "User Payment details are not provided yet...");
                Intent intent = new Intent(this, WalletOrBankConnectActivity.class);
                startActivity(intent);
            } else {
                Log.d(TAG, "User payment details are provided...");
            }
        }
    }

    private void showProfile() {
        onClickTab(findViewById(R.id.home_rl_profile));
    }

    private void showGroups() {
        onClickTab(findViewById(R.id.home_rl_group));
    }

    private void showChallenges() {
        onClickTab(findViewById(R.id.home_rl_challenges));
    }

//    private void getunReadNotificationCount() {
//
//        Cursor cur = getContext().getContentResolver().query(
//                MoEDataContract.MessageEntity.getContentUri(getApplicationContext()),
//                MoEDataContract.MessageEntity.PROJECTION, MoEDataContract.MessageEntity.MSG_CLICKED + " = ?",
//                new String[]{"0"}, MoEDataContract.MessageEntity.DEFAULT_SORT_ORDER);
//
//        int unReadCount = 0;
//        if (null != cur) {
//            unReadCount = cur.getCount();
//            cur.close();
//        }
//
//        CustomButton notificationCount = (CustomButton) findViewById(R.id.home_ibtn_notification_count);
//
//        if (unReadCount == 0) {
//            notificationCount.setVisibility(View.GONE);
//        } else {
//            notificationCount.setVisibility(View.VISIBLE);
//            notificationCount.setText(String.valueOf(unReadCount));
//        }
//    }

    public void onClickTab(View view) {
        switch (view.getId()) {
            case R.id.home_rl_challenges:
                setSelected(findViewById(R.id.home_ibtn_challenge), findViewById(R.id.home_tv_challenge));
                loadFragment(mCurrentFragment = AllChallengesFragment.newInstance());
                break;
            case R.id.home_rl_group:
                setSelected(findViewById(R.id.home_ibtn_group), findViewById(R.id.home_tv_group));
                loadFragment(mCurrentFragment = AllGroupsFragment.newInstance());
                break;
            case R.id.home_rl_leaderboard:
                setSelected(findViewById(R.id.home_ibtn_leaderboard), findViewById(R.id.home_tv_leaderboard));
                loadFragment(mCurrentFragment = new LBLandingFragment());
                break;
            case R.id.home_rl_profile:
                setSelected(findViewById(R.id.home_ibtn_profile), findViewById(R.id.home_tv_profile));

                if (null == NostragamusDataHandler.getInstance().getUserId()) {
                    navigateToLogIn();
                    return;
                }

                loadFragment(mCurrentFragment = ProfileFragment.newInstance(mProfileTabPosition, getIntent().getExtras()));
//                mProfileTabPosition = 0;
                break;
        }
    }

    private void setSelected(View selImg, View selText) {
        if (null != mSelectedImage) {
            mSelectedImage.setSelected(false);
        }

        if (null != mSelectedText) {
            mSelectedText.setSelected(false);
        }

        mSelectedImage = selImg;
        mSelectedImage.setSelected(true);

        mSelectedText = selText;
        mSelectedText.setSelected(true);
    }

    /*private void showJoinGroupAlert(final String groupCode, String groupName) {
        new AlertDialog.Builder(this)
                .setTitle("Group Invitation")
                .setMessage("You clicked on a group invitation link from \"" + groupName + "\" group, Would you like to proceed with joining this group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToJoinGroup(groupCode);
                    }
                })
                .setNegativeButton("No, thanks", null)
                .setCancelable(false)
                .create().show();
    }

    private void navigateToJoinGroup(String groupCode) {
        Intent intent = new Intent(this, JoinGroupActivity.class);
        intent.putExtra(BundleKeys.GROUP_CODE, groupCode);
        startActivity(intent);
    }*/

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fl_holder, fragment).commit();
    }

    private void showFirstTab() {
        onClickTab(findViewById(R.id.home_rl_challenges));
    }

    private void navigateToLogIn() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (CODE_PROFILE_ACTIVITY == requestCode) {
                loadFragment(new ProfileFragment());
            }
        }
    }

    /**
     * Used to get User Info from server, which will provide payment bank / paytm details
     * so that it can be identified that to show Connect to Paytm/Bank screen to capture such data
     * @return
     */
    private UserInfoModelImpl.OnGetUserInfoModelListener getUserInfoCallBackListener() {
        return new UserInfoModelImpl.OnGetUserInfoModelListener() {
            @Override
            public void onSuccessGetUpdatedUserInfo(UserInfo userInfo) {
                if (userInfo != null) {
                    checkPaymentInfoProvidedOrRequired(userInfo);
                } else {
                    Log.d(TAG, "User Payment info null");
                    // Do not perform any action
                }
            }

            @Override
            public void onFailedGetUpdateUserInfo(String message) {
                // Initial call, Api fails but can not be taken action
                Log.d(TAG, "Get UserInfo API failed.");
            }

            @Override
            public void onNoInternet() {
                // Initial call, Not required to show any msg
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (null != mCurrentFragment
                && mCurrentFragment instanceof LBLandingFragment
                && ((LBLandingFragment) mCurrentFragment).onBack()) {
            return;
        } else if (mCurrentFragment instanceof AllChallengesFragment) {
            super.onBackPressed();
        } else {
            showFirstTab();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.HOME;
    }

    @Override
    public void onClickChallenges() {
        showChallenges();
    }

    @Override
    public void onDismiss(int requestCode, Bundle bundle) {
        switch (requestCode) {
            case JOIN_CHALLENGE_REQUEST_CODE:
                Challenge mChallengeInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE_DATA));
                showJoinedChallenge(getContext(),mChallengeInfo);
                break;

            case CHALLENGE_JOINED_INFO_REQUEST_CODE:
                showChallenges(); // This will refresh the screen
                break;
        }
    }

    private void showJoinedChallenge(Context context, Challenge challenge) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        ChallengeInfoDialogFragment.newInstance(CHALLENGE_JOINED_INFO_REQUEST_CODE, "JOINED CHALLENGE!", challenge)
                .show(fragmentManager, "challenge_info");
    }
}