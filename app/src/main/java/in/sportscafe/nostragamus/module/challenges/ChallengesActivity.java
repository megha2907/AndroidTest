package in.sportscafe.nostragamus.module.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

import static io.fabric.sdk.android.services.concurrency.AsyncTask.init;

public class ChallengesActivity extends NostraBaseActivity {

    private static final String TAG = ChallengesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        initialize();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        initialize();
    }

    private void initialize() {
        loadFragment();
    }

    private void loadFragment() {
        Bundle args = new Bundle();
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        ChallengesFragment challengesFragment = new ChallengesFragment();
        challengesFragment.setArguments(args);

        FragmentHelper.replaceFragment(this, R.id.fragment_container, challengesFragment);
    }


}
