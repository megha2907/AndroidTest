package in.sportscafe.nostragamus.module.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;
import com.moe.pushlibrary.providers.MoEDataContract;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;
import in.sportscafe.nostragamus.module.tournament.TournamentFragment;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingFragment;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileFragment;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends NostragamusActivity implements OnHomeActionListener,UserInfoModelImpl.OnGetUserInfoModelListener {

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private View mSelectedTab;

    private ImageView mHomeButton;

    private ImageView mProfileButton;

    private ImageView mNotificationButton;

    private ImageView mLeaderBoardButton;

    private TextView mHomeTv;

    private TextView mProfileTv;

    private TextView mNotificationTv;

    private TextView mLeaderBoardTv;

    private RelativeLayout mHomeRl;

    private RelativeLayout mProfileRl;

    private RelativeLayout mNotificationRl;

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
        mNotificationButton = (ImageView) findViewById(R.id.home_ibtn_notification);
        mLeaderBoardButton = (ImageView) findViewById(R.id.home_ibtn_leaderboard);

        mHomeTv = (TextView) findViewById(R.id.home_tv_feed);
        mProfileTv = (TextView) findViewById(R.id.home_tv_profile);
        mNotificationTv = (TextView) findViewById(R.id.home_tv_notification);
        mLeaderBoardTv = (TextView) findViewById(R.id.home_tv_leaderboard);

        mHomeRl = (RelativeLayout) findViewById(R.id.home_rl_feed);
        mProfileRl = (RelativeLayout) findViewById(R.id.home_rl_profile);
        mNotificationRl = (RelativeLayout) findViewById(R.id.home_rl_notification);
        mLeaderBoardRl = (RelativeLayout) findViewById(R.id.home_rl_leaderboard);

        Intent intent = getIntent();
        if (null == intent.getExtras()) {
            showFeed();
        } else if (intent.getExtras().containsKey("group") || intent.getExtras().containsKey("results") || intent.getExtras().containsKey("badges")) {

            mHomeButton.setSelected(false);
            mNotificationButton.setSelected(false);
            mProfileButton.setSelected(true);

            mHomeTv.setSelected(false);
            mNotificationTv.setSelected(false);
            mProfileTv.setSelected(true);

//            mProfileRl.setBackgroundColor(Color.BLACK);
//            mProfileRl.getBackground().setAlpha(40);
//
//            mHomeRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
//            mNotificationRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));

            loadFragment(new ProfileFragment());
        } else {
            showFeed();
        }

        checkGroupCode();

        // getunReadNotificationCount();

    }

    private void getunReadNotificationCount() {

        Cursor cur = getContext().getContentResolver().query(
                MoEDataContract.MessageEntity.getContentUri(getApplicationContext()),
                MoEDataContract.MessageEntity.PROJECTION, MoEDataContract.MessageEntity.MSG_CLICKED + " = ?",
                new String[]{"0"}, MoEDataContract.MessageEntity.DEFAULT_SORT_ORDER);

        int unReadCount = 0;
        if (null != cur) {
            unReadCount = cur.getCount();
            cur.close();
        }

        CustomButton notificationCount = (CustomButton) findViewById(R.id.home_ibtn_notification_count);

        if (unReadCount == 0) {
            notificationCount.setVisibility(View.GONE);
        } else {
            notificationCount.setVisibility(View.VISIBLE);
            notificationCount.setText(String.valueOf(unReadCount));
        }
    }

    public void onClickTab(View view) {
        switch (view.getId()) {
            case R.id.home_rl_notification:

                mHomeButton.setSelected(false);
                mProfileButton.setSelected(false);
                mLeaderBoardButton.setSelected(false);
                mNotificationButton.setSelected(true);

                mHomeTv.setSelected(false);
                mProfileTv.setSelected(false);
                mLeaderBoardTv.setSelected(false);
                mNotificationTv.setSelected(true);

                  /*mNotificationRl.setBackgroundColor(Color.BLACK);
                  mNotificationRl.getBackground().setAlpha(40);

                  mHomeRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mProfileRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mLeaderBoardRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));*/

                loadFragment(mCurrentFragment = new AllGroupsFragment().newInstance());
                break;

            case R.id.home_rl_feed:

                mNotificationButton.setSelected(false);
                mProfileButton.setSelected(false);
                mLeaderBoardButton.setSelected(false);
                mHomeButton.setSelected(true);

                mNotificationTv.setSelected(false);
                mProfileTv.setSelected(false);
                mLeaderBoardTv.setSelected(false);
                mHomeTv.setSelected(true);

                  /*mHomeRl.setBackgroundColor(Color.BLACK);
                  mHomeRl.getBackground().setAlpha(40);

                  mProfileRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mNotificationRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mLeaderBoardRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));*/

                loadFragment(mCurrentFragment = new TournamentFragment());
                break;

            case R.id.home_rl_profile:

                mHomeButton.setSelected(false);
                mNotificationButton.setSelected(false);
                mLeaderBoardButton.setSelected(false);
                mProfileButton.setSelected(true);

                mHomeTv.setSelected(false);
                mNotificationTv.setSelected(false);
                mLeaderBoardTv.setSelected(false);
                mProfileTv.setSelected(true);

                  /*mProfileRl.setBackgroundColor(Color.BLACK);
                  mProfileRl.getBackground().setAlpha(40);

                  mHomeRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mNotificationRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mLeaderBoardRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));*/

                if (null == NostragamusDataHandler.getInstance().getUserId()) {
                    navigateToLogIn();
                    return;
                }
                loadFragment(mCurrentFragment = new ProfileFragment());
                break;

            case R.id.home_rl_leaderboard:

                mNotificationButton.setSelected(false);
                mProfileButton.setSelected(false);
                mHomeButton.setSelected(false);
                mLeaderBoardButton.setSelected(true);

                mNotificationTv.setSelected(false);
                mProfileTv.setSelected(false);
                mHomeTv.setSelected(false);
                mLeaderBoardTv.setSelected(true);

                  /*mLeaderBoardRl.setBackgroundColor(Color.BLACK);
                  mLeaderBoardRl.getBackground().setAlpha(40);

                  mProfileRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mNotificationRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));
                  mHomeRl.setBackgroundColor(ContextCompat.getColor(isThreadAlive(), R.color.colorMedium));*/

                loadFragment(mCurrentFragment = new LBLandingFragment());
                break;
        }
    }

    private void checkGroupCode() {
        String groupCode = NostragamusDataHandler.getInstance().getInstallGroupCode();
        if (null != groupCode) {
            NostragamusDataHandler.getInstance().setInstallGroupCode(null);

            showJoinGroupAlert(groupCode, NostragamusDataHandler.getInstance().getInstallGroupName());
        }
    }

    private void showJoinGroupAlert(final String groupCode, String groupName) {
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
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fl_holder, fragment).commit();
    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickPlay() {
        showFeed();
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