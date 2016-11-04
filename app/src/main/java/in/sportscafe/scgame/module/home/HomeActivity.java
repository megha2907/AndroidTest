package in.sportscafe.scgame.module.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.TournamentFeed.TournamentFeedFragment;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.notifications.NotificationInboxFragment;
import in.sportscafe.scgame.module.user.group.allgroups.AllGroups;
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

    private static final float ENABLE_TAB_ALPHA = 0.3f;

    private static final float DISABLE_TAB_ALPHA = 1f;

    private View mSelectedTab;

    private boolean feedShowing = false;

    private Toolbar mtoolbar;

    private TextView mTitle;

    private ImageView mLogo;

    private ImageButton mHomeButton;

    private ImageButton mProfileButton;

    private ImageButton mNotificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHomeButton=(ImageButton)findViewById(R.id.home_ibtn_feed);
        mProfileButton=(ImageButton)findViewById(R.id.home_ibtn_profile);
        mNotificationButton=(ImageButton)findViewById(R.id.home_ibtn_notification);

       // initToolBar();
        showFeed();
        getUserInfoFromServer();
    }

    public void onClickTab(View view) {
        feedShowing = false;

          switch (view.getId()) {
              case R.id.home_ibtn_notification:

                  //mtoolbar.setVisibility(View.VISIBLE);
                  feedShowing = true;
//                  mTitle.setVisibility(View.VISIBLE);
//                  mLogo.setVisibility(View.GONE);
//                  mTitle.setText("Notifications");

                  mHomeButton.setSelected(false);
                  mProfileButton.setSelected(false);
                  mNotificationButton.setSelected(true);

                  loadFragment(new NotificationInboxFragment());
                  break;
              case R.id.home_ibtn_feed:
//                  mtoolbar.setVisibility(View.VISIBLE);
//                  mLogo.setVisibility(View.VISIBLE);
//                  mTitle.setVisibility(View.GONE);

                  mNotificationButton.setSelected(false);
                  mProfileButton.setSelected(false);
                  mHomeButton.setSelected(true);

                  loadFragment(new TournamentFeedFragment());
                  break;
              case R.id.home_ibtn_profile:
//                  mtoolbar.setVisibility(View.GONE);

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

        if(null != mSelectedTab) {
            setTabEnabled(mSelectedTab, true);

        }
        mSelectedTab = view;
        setTabEnabled(mSelectedTab, false);
    }

    private void setTabEnabled(View tabView, boolean enabled) {
        tabView.setEnabled(enabled);
        if(enabled) {
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

//    @Override
//    public void onBackPressed() {
//        if(feedShowing) {
//            super.onBackPressed();
//        } else {
//            showFeed();
//        }
//    }

    private void showFeed() {
        onClickTab(findViewById(R.id.home_ibtn_feed));
    }

    private void navigateToLogIn() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    public void getUserInfoFromServer() {
        if(ScGame.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserInfoRequest(ScGameDataHandler.getInstance().getUserId()).enqueue(
                    new ScGameCallBack<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            if(response.isSuccessful()) {
                                UserInfo updatedUserInfo = response.body().getUserInfo();

                                if(null != updatedUserInfo) {

                                    ScGameDataHandler.getInstance().setUserInfo(updatedUserInfo);
                                    ScGameDataHandler.getInstance().setNumberofPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                    ScGameDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());


                                    List<AllGroups> newAllGroups = updatedUserInfo.getAllGroups();
                                    if(null != newAllGroups && newAllGroups.size() > 0) {
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

//    public void initToolBar() {
//        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
//        mtoolbar = (Toolbar) findViewById(R.id.home_toolbar);
//        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
//        mLogo = (ImageView) mtoolbar.findViewById(R.id.toolbar_logo);
//        mTitle.setTypeface(tftitle);
//        setSupportActionBar(mtoolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//    }

}