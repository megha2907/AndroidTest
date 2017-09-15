package in.sportscafe.nostragamus.module.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    /**
     * Sets Immersive mode
     * Status bar and navigation bar gets hidden
     */
    protected void setImmersiveFullScreenMode() {
        int visibility = View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        Window window = getWindow();
        if (window != null) {
            window.getDecorView().setSystemUiVisibility(visibility);

        /* set transparent system bars */
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            setSystemUiChangeListenerForImmersiveMode(visibility, window);
        }
    }

    private void setSystemUiChangeListenerForImmersiveMode(final int visibilityApplied, Window window) {
        final Runnable hideSystemUiCallback = new Runnable() {
            @Override
            public void run() {
                setImmersiveFullScreenMode();
            }
        };

        final Handler mSystemUiHandler = new Handler();
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & visibilityApplied) == 0) {
                            mSystemUiHandler.removeCallbacks(hideSystemUiCallback);
                            mSystemUiHandler.postDelayed(hideSystemUiCallback, 1000);
                        }
                    }
                });
    }


}
