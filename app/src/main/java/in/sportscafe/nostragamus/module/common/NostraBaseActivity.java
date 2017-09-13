package in.sportscafe.nostragamus.module.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.sportscafe.nostragamus.R;

/**
 * Base Activity for all New Activities now-onwards
 */
public class NostraBaseActivity extends AppCompatActivity {

    private boolean mShouldAnimateActivity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performActivityEntryAnimation();
//        setContentView(R.layout.activity_nostra_base);

    }

    /**
     * Set to true for activity if animation for entry/exit required
     *
     * @param shouldAnimate
     */
    protected void setShouldAnimateActivity(boolean shouldAnimate) {
        mShouldAnimateActivity = shouldAnimate;
    }

    private void performActivityExitAnimation() {
        if (mShouldAnimateActivity) {
            overridePendingTransition(R.anim.activity_anim_stay, R.anim.slide_right_from_right);
        }
    }

    private void performActivityEntryAnimation() {
        if (mShouldAnimateActivity) {
            overridePendingTransition(R.anim.slide_left_from_right, R.anim.activity_anim_stay);
        }
    }

    @Override
    public void finish() {
        super.finish();
        performActivityExitAnimation();
    }

}
