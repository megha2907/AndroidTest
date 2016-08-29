package in.sportscafe.scgame.module.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.feed.FeedFragment;
import in.sportscafe.scgame.module.notifications.NotificationInboxFragment;
import in.sportscafe.scgame.module.play.PlayFragment;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.ProfileFragment;
import in.sportscafe.scgame.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.scgame.module.user.sportselection.dto.AllSports;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;
import in.sportscafe.scgame.module.user.sportselection.dto.UserSports;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolBar();
        showFeed();
    }

    public void onClickTab(View view) {
        feedShowing = false;

          switch (view.getId()) {
              case R.id.home_ibtn_feed:
                  feedShowing = true;
                   mTitle.setText("Notifications");
                  loadFragment(new NotificationInboxFragment());
                  break;
              case R.id.home_ibtn_play:
                  mTitle.setText("Feed");
                  loadFragment(new PlayFragment());
                  break;
              case R.id.home_ibtn_profile:
                  mTitle.setText("Profile");
                  if (null == ScGameDataHandler.getInstance().getUserId()) {
                      navigateToLogIn();
                      return;
                  }
                  getUserInfoFromServer();
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
            tabView.setAlpha(ENABLE_TAB_ALPHA);
        } else {
            tabView.setAlpha(DISABLE_TAB_ALPHA);
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

    @Override
    public void onBackPressed() {
        if(feedShowing) {
            super.onBackPressed();
        } else {
            showFeed();
        }
    }

    private void showFeed() {
        onClickTab(findViewById(R.id.home_ibtn_play));
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

                                }

                            }
                        }
                    }
            );
        }
    }

    public void initToolBar() {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        mtoolbar = (Toolbar) findViewById(R.id.home_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Feed");
        mTitle.setTypeface(tftitle);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}