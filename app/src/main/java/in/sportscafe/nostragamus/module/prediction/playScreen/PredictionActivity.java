package in.sportscafe.nostragamus.module.prediction.playScreen;

import android.content.Intent;
import android.os.Bundle;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.cache.CacheManagementHelper;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PredictionActivity extends NostraBaseActivity implements PredictionFragmentListener  {

    private static final String TAG = PredictionActivity.class.getSimpleName();

    public interface LaunchedFrom {
        int NEW_CHALLENGES_SCREEN_PSEUDO_PLAY = 111;
        int IN_PLAY_SCREEN_PLAY_MATCH = 112;
        int DUMMY_GAME = 113;
    }

    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_PLAY;
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

    @Override
    public void onMatchCompleted(Bundle args) {
        /* Start My result activity */
        Intent intent = new Intent(this, MyResultsActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        /* If playing pseudoGame, launch inplay-headless state Matched screen */
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle args = getIntent().getExtras();
            if (args.containsKey(Constants.BundleKeys.PLAY_SCREEN_DATA)) {
                PlayScreenDataDto playScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA));
                if (playScreenData != null && playScreenData.isPlayingPseudoGame()) {
                        Intent clearTaskIntent = new Intent(this, NostraHomeActivity.class);
                        clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);
                        clearTaskIntent.putExtras(args);
                        clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(clearTaskIntent);
                }
            }
        }

        super.onBackPressed();
    }
}
