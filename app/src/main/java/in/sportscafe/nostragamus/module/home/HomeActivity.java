package in.sportscafe.nostragamus.module.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.moe.pushlibrary.providers.MoEDataContract;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.notifications.NotificationInboxFragment;
import in.sportscafe.nostragamus.module.tournament.TournamentFragment;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileFragment;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends NostragamusActivity implements OnHomeActionListener {

    private static final int CODE_PROFILE_ACTIVITY = 1;

    private View mSelectedTab;

    private ImageView mHomeButton;

    private ImageView mProfileButton;

    private ImageView mNotificationButton;

    private TextView mHomeTv;

    private TextView mProfileTv;

    private TextView mNotificationTv;

    private RelativeLayout mHomeRl;

    private RelativeLayout mProfileRl;

    private RelativeLayout mNotificationRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (null == NostragamusDataHandler.getInstance().getUserId()) {
            navigateToLogIn();
            return;
        }

        Log.i("cookie",NostragamusDataHandler.getInstance().getCookie());

        getUserInfoFromServer();
        mHomeButton=(ImageView)findViewById(R.id.home_ibtn_feed);
        mProfileButton=(ImageView)findViewById(R.id.home_ibtn_profile);
        mNotificationButton=(ImageView)findViewById(R.id.home_ibtn_notification);

        mHomeTv=(TextView)findViewById(R.id.home_tv_feed);
        mProfileTv=(TextView)findViewById(R.id.home_tv_profile);
        mNotificationTv=(TextView)findViewById(R.id.home_tv_notification);

        mHomeRl=(RelativeLayout) findViewById(R.id.home_rl_feed);
        mProfileRl=(RelativeLayout) findViewById(R.id.home_rl_profile);
        mNotificationRl=(RelativeLayout) findViewById(R.id.home_rl_notification);

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

            mProfileRl.setBackgroundColor(Color.BLACK);
            mProfileRl.getBackground().setAlpha(40);

            mHomeRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));
            mNotificationRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));

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

                  mNotificationRl.setBackgroundColor(Color.BLACK);
                  mNotificationRl.getBackground().setAlpha(40);

                  mHomeRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));
                  mProfileRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));

                  loadFragment(new NotificationInboxFragment());
                  break;

              case R.id.home_rl_feed:

                  mNotificationButton.setSelected(false);
                  mProfileButton.setSelected(false);
                  mHomeButton.setSelected(true);

                  mNotificationTv.setSelected(false);
                  mProfileTv.setSelected(false);
                  mHomeTv.setSelected(true);

                  mHomeRl.setBackgroundColor(Color.BLACK);
                  mHomeRl.getBackground().setAlpha(40);

                  mProfileRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));
                  mNotificationRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));

                  loadFragment(new TournamentFragment());
                  break;

              case R.id.home_rl_profile:

                mHomeButton.setSelected(false);
                  mNotificationButton.setSelected(false);
                  mProfileButton.setSelected(true);

                  mHomeTv.setSelected(false);
                  mNotificationTv.setSelected(false);
                  mProfileTv.setSelected(true);


                  mProfileRl.setBackgroundColor(Color.BLACK);
                  mProfileRl.getBackground().setAlpha(40);

                  mHomeRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));
                  mNotificationRl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMedium));

                  if (null == NostragamusDataHandler.getInstance().getUserId()) {
                      navigateToLogIn();
                      return;
                  }
                  loadFragment(new ProfileFragment());
                  break;
          }
    }

    private void checkGroupCode() {
        String groupCode = NostragamusDataHandler.getInstance().getInstallGroupCode();
        if(null != groupCode) {
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
    public void getUserInfoFromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserInfoRequest(NostragamusDataHandler.getInstance().getUserId()).enqueue(
                    new NostragamusCallBack<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            if (response.isSuccessful()) {
                                UserInfo updatedUserInfo = response.body().getUserInfo();

                                if (null != updatedUserInfo) {

                                    NostragamusDataHandler.getInstance().setUserInfo(updatedUserInfo);
                                    if(null != updatedUserInfo)
                                    {
                                        NostragamusDataHandler.getInstance().setNumberof2xPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                        NostragamusDataHandler.getInstance().setNumberofNonegsPowerups(updatedUserInfo.getPowerUps().get("no_negs"));
                                        NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(updatedUserInfo.getPowerUps().get("player_poll"));
                                    }
                                    NostragamusDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());
                                    NostragamusDataHandler.getInstance().setNumberofGroups(updatedUserInfo.getNumberofgroups());
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