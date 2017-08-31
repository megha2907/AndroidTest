package in.sportscafe.nostragamus.module.newChallenges.ui;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class NewChallengesActivity extends NostraHomeActivity {

    private static final String TAG = NewChallengesActivity.class.getSimpleName();
    private NewChallengesFragment mNewChallengesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNewChallengesSelected();
        setContentLayout(R.layout.activity_challenges);
        initialize();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passNewIntentDataToFragment(intent);
    }

    private void passNewIntentDataToFragment(Intent intent) {
        if (intent != null && mNewChallengesFragment != null) {
            // Build bundle if required

            mNewChallengesFragment.onNewIntent(intent);
        }
    }

    private void initialize() {
        loadFragment();
    }

    private void loadFragment() {
        Bundle args = new Bundle();
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mNewChallengesFragment = new NewChallengesFragment();
        mNewChallengesFragment.setArguments(args);

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mNewChallengesFragment);
    }


}
