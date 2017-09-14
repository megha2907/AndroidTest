package in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 9/13/17.
 */

public class ContestDetailsAfterJoinActivity extends NostraBaseActivity implements ContestDetailsAJFragmentListener {

    private static final String TAG = ContestDetailsAfterJoinActivity.class.getSimpleName();

    private ContestDetailsAfterJoinFragment mContestDetailsAfterJoinFragment;

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

        mContestDetailsAfterJoinFragment = new ContestDetailsAfterJoinFragment();
        if (args != null) {
            mContestDetailsAfterJoinFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mContestDetailsAfterJoinFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passIntentToFragment(intent);
    }

    private void passIntentToFragment(Intent intent) {
        if (intent != null && mContestDetailsAfterJoinFragment != null) {
            mContestDetailsAfterJoinFragment.onNewIntent(intent);
        }
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
