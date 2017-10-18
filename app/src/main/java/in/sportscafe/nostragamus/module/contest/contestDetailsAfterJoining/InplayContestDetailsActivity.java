package in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining;

import android.content.Intent;
import android.os.Bundle;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 9/13/17.
 */

public class InplayContestDetailsActivity extends NostraBaseActivity implements ContestDetailsAJFragmentListener {

    private static final String TAG = InplayContestDetailsActivity.class.getSimpleName();

    private InPlayContestDetailsFragment mInPlayContestDetailsFragment;

    private int Launched_From_Results = 112;

    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_IN_PLAY_CONTEST_DETAILS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inplay_contest_details);
        loadFragment();
    }

    private void loadFragment() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mInPlayContestDetailsFragment = new InPlayContestDetailsFragment();
        if (args != null) {
            mInPlayContestDetailsFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mInPlayContestDetailsFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passIntentToFragment(intent);
    }

    private void passIntentToFragment(Intent intent) {
        if (intent != null && mInPlayContestDetailsFragment != null) {
            mInPlayContestDetailsFragment.onNewIntent(intent);
        }
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {

        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
            if (args.containsKey(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT)) {

                if (args.getInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT) == Launched_From_Results) {

                    finishStackAndLaunchHomeInPlay();

                } else {
                    finish();
                }
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    private void finishStackAndLaunchHomeInPlay() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        Intent clearTaskIntent = new Intent(getApplicationContext(), NostraHomeActivity.class);
        clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);
        clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (args != null) {
            clearTaskIntent.putExtras(args);
        }
        startActivity(clearTaskIntent);
    }
}
