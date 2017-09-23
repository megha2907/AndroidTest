package in.sportscafe.nostragamus.module.prediction.playScreen;

import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PredictionActivity extends NostraBaseActivity implements PredictionFragmentListener  {

    private static final String TAG = PredictionActivity.class.getSimpleName();

    public interface LaunchedFrom {
        int NEW_CHALLENGES_SCREEN_PSEUDO_PLAY = 111;
        int IN_PLAY_SCREEN_PLAY_MATCH = 121;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction2);
        setImmersiveFullScreenMode();

        loadFragment();
    }

    private void loadFragment() {
        PredictionFragment predictionFragment = new PredictionFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            predictionFragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, predictionFragment);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

}
