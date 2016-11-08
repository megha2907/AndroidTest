package in.sportscafe.scgame.module.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import in.sportscafe.scgame.Constants.BundleKeys;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.TournamentFeed.TournamentFeedFragment;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.notifications.NotificationInboxFragment;
import in.sportscafe.scgame.module.user.group.allgroups.AllGroups;
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

    private View mSelectedTab;

    private ImageButton mHomeButton;

    private ImageButton mProfileButton;

    private ImageButton mNotificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHomeButton = (ImageButton) findViewById(R.id.home_ibtn_feed);
        mProfileButton = (ImageButton) findViewById(R.id.home_ibtn_profile);
        mNotificationButton = (ImageButton) findViewById(R.id.home_ibtn_notification);

        showFeed();
        getUserInfoFromServer();

        checkGroupCode();
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

    public void onClickTab(View view) {
        switch (view.getId()) {
            case R.id.home_ibtn_notification:
                mHomeButton.setSelected(false);
                mProfileButton.setSelected(false);
                mNotificationButton.setSelected(true);

                loadFragment(new NotificationInboxFragment());
                break;
            case R.id.home_ibtn_feed:
                mNotificationButton.setSelected(false);
                mProfileButton.setSelected(false);
                mHomeButton.setSelected(true);

                loadFragment(new TournamentFeedFragment());
                break;
            case R.id.home_ibtn_profile:
                mHomeButton.setSelected(false);
                mNotificationButton.setSelected(false);
                mProfileButton.setSelected(true);

                if (null == ScGameDataHandler.getInstance().getUserId()) {
                    navigateToLogIn();
                    return;
                }
                loadFragment(new ProfileFragment());
                break;

        }

        if (null != mSelectedTab) {
            setTabEnabled(mSelectedTab, true);

        }
        mSelectedTab = view;
        setTabEnabled(mSelectedTab, false);
    }

    private void setTabEnabled(View tabView, boolean enabled) {
        tabView.setEnabled(enabled);
        if (enabled) {
        } else {
        }
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
        onClickTab(findViewById(R.id.home_ibtn_feed));
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
                                    ScGameDataHandler.getInstance().setNumberofPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                    ScGameDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());


                                    List<AllGroups> newAllGroups = updatedUserInfo.getAllGroups();
                                    if (null != newAllGroups && newAllGroups.size() > 0) {
                                        List<AllGroups> oldAllGroupsList = ScGameDataHandler.getInstance().getAllGroups();
                                        oldAllGroupsList.clear();
                                        for (AllGroups allGroups : newAllGroups) {
                                            if (!oldAllGroupsList.contains(allGroups)) {
                                                oldAllGroupsList.add(allGroups);
                                            }
                                        }
                                        ScGameDataHandler.getInstance().setAllGroups(oldAllGroupsList);
                                        ScGameDataHandler.getInstance().setNumberofGroups(oldAllGroupsList.size());

                                    }
                                }
                            }
                        }
                    }
            );
        }
    }
}