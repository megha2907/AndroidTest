package in.sportscafe.nostragamus.module.prediction;

import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PredictionActivity extends NostraBaseActivity  {

    private static final String TAG = PredictionActivity.class.getSimpleName();

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

}
