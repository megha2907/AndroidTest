package in.sportscafe.scgame.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.feed.FeedFragment;
import in.sportscafe.scgame.module.play.PlayFragment;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.myprofile.ProfileFragment;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends ScGameActivity implements OnHomeActionListener {

    private static final float ENABLE_TAB_ALPHA = 0.3f;

    private static final float DISABLE_TAB_ALPHA = 1f;

    private View mSelectedTab;

    private boolean feedShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showFeed();
    }

    public void onClickTab(View view) {
        feedShowing = false;

          switch (view.getId()) {
              case R.id.home_ibtn_feed:
                  feedShowing = true;
                  loadFragment(new FeedFragment());
                  break;
              case R.id.home_ibtn_play:
                  loadFragment(new PlayFragment());
                  break;
              case R.id.home_ibtn_profile:
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("startplay").equals("play")){
            Log.i("intent","play");
            onClickTab(findViewById(R.id.home_ibtn_play));
        }
    }
}