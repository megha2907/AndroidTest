package in.sportscafe.nostragamus.module.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.tournament.TournamentFragment;
import in.sportscafe.nostragamus.module.user.group.JoinGroupApiModelImpl;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingFragment;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileFragment;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends NostragamusActivity implements UserInfoModelImpl.OnGetUserInfoModelListener {

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private View mSelectedTab;

    private ImageView mHomeButton;

    private ImageView mProfileButton;

    private ImageView mGroupButton;

    private ImageView mLeaderBoardButton;

    private TextView mHomeTv;

    private TextView mProfileTv;

    private TextView mGroupTv;

    private TextView mLeaderBoardTv;

    private RelativeLayout mHomeRl;

    private RelativeLayout mProfileRl;

    private RelativeLayout mGroupRl;

    private RelativeLayout mLeaderBoardRl;

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


        mHomeButton = (ImageView) findViewById(R.id.home_ibtn_feed);
        mProfileButton = (ImageView) findViewById(R.id.home_ibtn_profile);
        mGroupButton = (ImageView) findViewById(R.id.home_ibtn_group);
        mLeaderBoardButton = (ImageView) findViewById(R.id.home_ibtn_leaderboard);

        mHomeTv = (TextView) findViewById(R.id.home_tv_feed);
        mProfileTv = (TextView) findViewById(R.id.home_tv_profile);
        mGroupTv = (TextView) findViewById(R.id.home_tv_group);
        mLeaderBoardTv = (TextView) findViewById(R.id.home_tv_leaderboard);

        mHomeRl = (RelativeLayout) findViewById(R.id.home_rl_feed);
        mProfileRl = (RelativeLayout) findViewById(R.id.home_rl_profile);
        mGroupRl = (RelativeLayout) findViewById(R.id.home_rl_group);
        mLeaderBoardRl = (RelativeLayout) findViewById(R.id.home_rl_leaderboard);

        checkGroupCode();

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            if (bundle.containsKey(BundleKeys.RESULTS)
                    || bundle.containsKey(BundleKeys.BADGES)
                    || bundle.containsKey(BundleKeys.OPEN_PROFILE)) {
                showProfile();
                return;
            } else if (bundle.containsKey(BundleKeys.GROUP)) {
                showGroups();
                return;
            }
        }
        showFeed();
    }

    private void showProfile() {
        onClickTab(findViewById(R.id.home_rl_profile));
    }

    private void showGroups() {
        onClickTab(findViewById(R.id.home_rl_group));
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
            case R.id.home_rl_group:
                mHomeButton.setSelected(false);
                mProfileButton.setSelected(false);
                mLeaderBoardButton.setSelected(false);
                mGroupButton.setSelected(true);

                mHomeTv.setSelected(false);
                mProfileTv.setSelected(false);
                mLeaderBoardTv.setSelected(false);
                mGroupTv.setSelected(true);

                loadFragment(mCurrentFragment = new AllGroupsFragment().newInstance());
                break;
            case R.id.home_rl_feed:
                mGroupButton.setSelected(false);
                mProfileButton.setSelected(false);
                mLeaderBoardButton.setSelected(false);
                mHomeButton.setSelected(true);

                mGroupTv.setSelected(false);
                mProfileTv.setSelected(false);
                mLeaderBoardTv.setSelected(false);
                mHomeTv.setSelected(true);

                loadFragment(mCurrentFragment = TournamentFragment.newInstance());
                break;
            case R.id.home_rl_profile:
                mHomeButton.setSelected(false);
                mGroupButton.setSelected(false);
                mLeaderBoardButton.setSelected(false);
                mProfileButton.setSelected(true);

                mHomeTv.setSelected(false);
                mGroupTv.setSelected(false);
                mLeaderBoardTv.setSelected(false);
                mProfileTv.setSelected(true);

                if (null == NostragamusDataHandler.getInstance().getUserId()) {
                    navigateToLogIn();
                    return;
                }
                loadFragment(mCurrentFragment = new ProfileFragment());
                break;
            case R.id.home_rl_leaderboard:
                mGroupButton.setSelected(false);
                mProfileButton.setSelected(false);
                mHomeButton.setSelected(false);
                mLeaderBoardButton.setSelected(true);

                mGroupTv.setSelected(false);
                mProfileTv.setSelected(false);
                mHomeTv.setSelected(false);
                mLeaderBoardTv.setSelected(true);

                loadFragment(mCurrentFragment = new LBLandingFragment());
                break;
        }
    }

    private void checkGroupCode() {
        String groupCode = NostragamusDataHandler.getInstance().getInstallGroupCode();
        if (null != groupCode) {
            joinGroup(groupCode);
        }
    }

    private void joinGroup(String groupCode) {
        JoinGroupApiModelImpl.newInstance(new JoinGroupApiModelImpl.OnJoinGroupApiModelListener() {
            @Override
            public void onSuccessJoinGroupApi(GroupInfo groupInfo) {

            }

            @Override
            public void onFailedJoinGroupApi(String message) {

            }

            @Override
            public void onNoInternet() {

            }

            @Override
            public void onInvalidGroupCode() {

            }
        }).joinGroup(groupCode, true);
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

    private void showFeed() {
        onClickTab(findViewById(R.id.home_rl_feed));
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
        }
        super.onBackPressed();
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
}