package in.sportscafe.nostragamus.module.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.AllChallengesFragment;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingFragment;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileFragment;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends NostragamusActivity implements UserInfoModelImpl.OnGetUserInfoModelListener, OnHomeActionListener ,OnDismissListener {

    private static final int CODE_PROFILE_ACTIVITY = 1;

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

        UserInfoModelImpl.newInstance(this).getUserInfo();

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

    @Override
    public void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo) {

    }

    @Override
    public void onFailedGetUpdateUserInfo(String message) {

    }

    @Override
    public void onNoInternet() {

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
        if (requestCode == JOIN_CHALLENGE_REQUEST_CODE) {
            showChallenges();
        }
    }
}