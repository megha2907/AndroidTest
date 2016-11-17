package in.sportscafe.scgame.module.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.moe.pushlibrary.providers.MoEDataContract;

import in.sportscafe.scgame.Constants.BundleKeys;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.tournament.TournamentFragment;
import in.sportscafe.scgame.module.tournamentFeed.TournamentFeedFragment;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.notifications.NotificationInboxFragment;
import in.sportscafe.scgame.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.ProfileFragment;
import in.sportscafe.scgame.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends ScGameActivity implements OnHomeActionListener {

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private View mSelectedTab;

    private ImageView mHomeButton;

    private ImageView mProfileButton;

    private ImageView mNotificationButton;

    private TextView mHomeTv;

    private TextView mProfileTv;

    private TextView mNotificationTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (null == ScGameDataHandler.getInstance().getUserId()) {
            navigateToLogIn();
            return;
        }

        getUserInfoFromServer();
        mHomeButton=(ImageView)findViewById(R.id.home_ibtn_feed);
        mProfileButton=(ImageView)findViewById(R.id.home_ibtn_profile);
        mNotificationButton=(ImageView)findViewById(R.id.home_ibtn_notification);

        mHomeTv=(TextView)findViewById(R.id.home_tv_feed);
        mProfileTv=(TextView)findViewById(R.id.home_tv_profile);
        mNotificationTv=(TextView)findViewById(R.id.home_tv_notification);

        Intent intent = getIntent();
        if (null==intent.getExtras()){
            showFeed();
        }
        else if (intent.getExtras().containsKey("group")||intent.getExtras().containsKey("results")||intent.getExtras().containsKey("badges")){

            mHomeButton.setSelected(false);
            mNotificationButton.setSelected(false);
            mProfileButton.setSelected(true);

            mHomeTv.setSelected(false);
            mNotificationTv.setSelected(false);
            mProfileTv.setSelected(true);

            loadFragment(new ProfileFragment());
        }
        else {
            showFeed();
        }

        checkGroupCode();

        getunReadNotificationCount();

    }

    private void getunReadNotificationCount() {

        Cursor cur = getContext().getContentResolver().query(
                MoEDataContract.MessageEntity.getContentUri(getApplicationContext()),
                MoEDataContract.MessageEntity.PROJECTION, MoEDataContract.MessageEntity.MSG_CLICKED + " = ?",
                new String[] { "0" }, MoEDataContract.MessageEntity.DEFAULT_SORT_ORDER);

        int unReadCount = 0;
        if (null != cur) {
            unReadCount = cur.getCount();
            cur.close();
        }

        CustomButton notificationCount = (CustomButton)findViewById(R.id.home_ibtn_notification_count);

        if (unReadCount==0){
            notificationCount.setVisibility(View.GONE);
        } else{
            notificationCount.setVisibility(View.VISIBLE);
            notificationCount.setText(String.valueOf(unReadCount));
        }
    }

    public void onClickTab(View view) {
          switch (view.getId()) {
              case R.id.home_rl_notification:

                  mHomeButton.setSelected(false);
                  mProfileButton.setSelected(false);
                  mNotificationButton.setSelected(true);

                  mHomeTv.setSelected(false);
                  mProfileTv.setSelected(false);
                  mNotificationTv.setSelected(true);

                  loadFragment(new NotificationInboxFragment());
                  break;

              case R.id.home_rl_feed:

                  mNotificationButton.setSelected(false);
                  mProfileButton.setSelected(false);
                  mHomeButton.setSelected(true);

                  mNotificationTv.setSelected(false);
                  mProfileTv.setSelected(false);
                  mHomeTv.setSelected(true);

                  loadFragment(new TournamentFragment());
                  break;

              case R.id.home_rl_profile:

                mHomeButton.setSelected(false);
                  mNotificationButton.setSelected(false);
                  mProfileButton.setSelected(true);

                  mHomeTv.setSelected(false);
                  mNotificationTv.setSelected(false);
                  mProfileTv.setSelected(true);

                  if (null == ScGameDataHandler.getInstance().getUserId()) {
                      navigateToLogIn();
                      return;
                  }
                  loadFragment(new ProfileFragment());
                  break;
          }
    }

    private void checkGroupCode() {
        String groupCode = ScGameDataHandler.getInstance().getInstallGroupCode();
        if(null != groupCode) {
            ScGameDataHandler.getInstance().setInstallGroupCode(null);

            showJoinGroupAlert(groupCode);
        }
    }

    private void showJoinGroupAlert(final String groupCode) {
        new AlertDialog.Builder(this)
                .setTitle("Join Group")
                .setMessage("You got one group invitation, do you want to join in that group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToJoinGroup(groupCode);
                    }
                })
                .setNegativeButton("No", null)
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
    public void getUserInfoFromServer() {
        if (ScGame.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserInfoRequest(ScGameDataHandler.getInstance().getUserId()).enqueue(
                    new ScGameCallBack<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            if (response.isSuccessful()) {
                                UserInfo updatedUserInfo = response.body().getUserInfo();

                                if (null != updatedUserInfo) {

                                    ScGameDataHandler.getInstance().setUserInfo(updatedUserInfo);
                                    if(null != updatedUserInfo)
                                    {
                                        ScGameDataHandler.getInstance().setNumberofPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                    }
                                    ScGameDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());
                                    ScGameDataHandler.getInstance().setNumberofGroups(updatedUserInfo.getNumberofgroups());
                                }
                            }
                        }
                    }
            );
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK == resultCode) {
            Log.i("inside","resultCode");
            if(CODE_PROFILE_ACTIVITY == requestCode) {
                Log.i("inside","profile");
                loadFragment(new ProfileFragment());
            }
        }
    }
}