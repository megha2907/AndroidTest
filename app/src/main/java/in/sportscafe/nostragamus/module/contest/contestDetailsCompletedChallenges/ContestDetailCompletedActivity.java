package in.sportscafe.nostragamus.module.contest.contestDetailsCompletedChallenges;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by sc on 4/10/17.
 */

public class ContestDetailCompletedActivity extends NostraBaseActivity implements ContestDetailCompletedFragmentListener {

    private static final String TAG = ContestDetailCompletedActivity.class.getSimpleName();

    private ContestDetailCompletedFragment mContestDetailCompletedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aj_contest_details);
        loadFragment();
    }

    private void loadFragment() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mContestDetailCompletedFragment = new ContestDetailCompletedFragment();
        if (args != null) {
            mContestDetailCompletedFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mContestDetailCompletedFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passIntentToFragment(intent);
    }

    private void passIntentToFragment(Intent intent) {
        if (intent != null && mContestDetailCompletedFragment != null) {
            mContestDetailCompletedFragment.onNewIntent(intent);
        }
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
