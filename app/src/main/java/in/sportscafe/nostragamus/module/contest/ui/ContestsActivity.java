package in.sportscafe.nostragamus.module.contest.ui;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.newChallenges.ui.matches.NewChallengeMatchFragmentListener;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class ContestsActivity extends NostraBaseActivity {

    private static final String TAG = ContestsActivity.class.getSimpleName();

    public interface LaunchedFrom {
        int NEW_CHALLENGE_MATCHES = 211;
        int IN_PLAY_MATCHES = 212;
        int IN_PLAY_HEAD_LESS_MATCHES = 213;
    }

    private ContestFragment mContestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contests);
        loadFragment();
    }

    private void loadFragment() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mContestFragment = new ContestFragment();
        if (args != null) {
            mContestFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mContestFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passIntentToFragment(intent);
    }

    private void passIntentToFragment(Intent intent) {
        if (intent != null && mContestFragment != null) {
            mContestFragment.onNewIntent(intent);
        }
    }

}
