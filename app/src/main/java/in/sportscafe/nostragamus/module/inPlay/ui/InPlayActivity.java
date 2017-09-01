package in.sportscafe.nostragamus.module.inPlay.ui;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.ui.ContestFragment;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class InPlayActivity extends NostraHomeActivity {

    private static final String TAG = InPlayActivity.class.getSimpleName();

    private InPlayFragment mInPlayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJoinSelected();
        setContentLayout(R.layout.activity_join);
        loadFragment();
    }

    private void loadFragment() {
        /*Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mInPlayFragment = new InPlayFragment();
        if (args != null) {
            mInPlayFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mInPlayFragment);*/

        FragmentHelper.replaceFragment(this, R.id.fragment_container, new ContestFragment());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passNewIntentDataToFragment(intent);
    }

    private void passNewIntentDataToFragment(Intent intent) {
        if (intent != null && mInPlayFragment != null) {
            mInPlayFragment.onNewIntent(intent);
        }
    }
}
