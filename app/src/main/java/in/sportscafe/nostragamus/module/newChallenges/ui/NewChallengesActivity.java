package in.sportscafe.nostragamus.module.newChallenges.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class NewChallengesActivity extends NostraHomeActivity {

    private static final String TAG = NewChallengesActivity.class.getSimpleName();
    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;

    private NewChallengesFragment mNewChallengesFragment;
    private boolean mIsFirstBackPressed = false;

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

    @Override
    public void onBackPressed() {
        handleDoubleBackPressLogicToExit();
    }

    /**
     * Application exit happens only when user clicks back button twice within specified time interval
     */
    private void handleDoubleBackPressLogicToExit() {
        if (mIsFirstBackPressed) {
            super.onBackPressed();
        } else {

            Toast.makeText(this, getString(R.string.double_back_press_msg), Toast.LENGTH_SHORT).show();
            mIsFirstBackPressed = true;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsFirstBackPressed = false;
                }
            }, DOUBLE_BACK_PRESSED_DELAY_ALLOWED);
        }
    }


}
