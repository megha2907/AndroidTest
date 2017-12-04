package in.sportscafe.nostragamus.module.contest.poolContest;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PoolPrizesEstimationActivity extends NostraBaseActivity implements PoolPrizesEstimationFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.POOL_CONTEST_REWARD_CALCULATION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_contest_reward_calculation);
        loadFragment();
    }

    private void loadFragment() {
        PoolPrizesEstimationFragment fragment = new PoolPrizesEstimationFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            fragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }


    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
