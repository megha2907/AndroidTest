package in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 9/13/17.
 */

public class ContestDetailsAJActivity extends NostraBaseActivity implements ContestDetailsAJFragmentListener {

    private static final String TAG = ContestDetailsAJActivity.class.getSimpleName();

    private ContestDetailsAJFragment mContestDetailsAJFragment;


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

        mContestDetailsAJFragment = new ContestDetailsAJFragment();
        if (args != null) {
            mContestDetailsAJFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mContestDetailsAJFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passIntentToFragment(intent);
    }

    private void passIntentToFragment(Intent intent) {
        if (intent != null && mContestDetailsAJFragment != null) {
            mContestDetailsAJFragment.onNewIntent(intent);
        }
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
