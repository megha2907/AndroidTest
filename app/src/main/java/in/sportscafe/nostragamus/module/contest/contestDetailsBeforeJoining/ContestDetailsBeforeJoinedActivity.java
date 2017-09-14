package in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 9/10/17.
 */

public class ContestDetailsBeforeJoinedActivity extends NostraBaseActivity implements ContestDetailsBJFragmentListener {

    private static final String TAG = ContestDetailsBeforeJoinedActivity.class.getSimpleName();

    private ContestDetailsBeforeJoinFragment mContestDetailsBeforeJoinFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_details);
        loadFragment();
    }

    private void loadFragment() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mContestDetailsBeforeJoinFragment = new ContestDetailsBeforeJoinFragment();
        if (args != null) {
            mContestDetailsBeforeJoinFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mContestDetailsBeforeJoinFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passIntentToFragment(intent);
    }

    private void passIntentToFragment(Intent intent) {
        if (intent != null && mContestDetailsBeforeJoinFragment != null) {
            mContestDetailsBeforeJoinFragment.onNewIntent(intent);
        }
    }

    @Override
    public void onJoinContestClicked() {

    }

    @Override
    public void onBackBtnClicked() {
        onBackPressed();
    }
}