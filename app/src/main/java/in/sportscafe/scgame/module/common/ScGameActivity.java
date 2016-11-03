package in.sportscafe.scgame.module.common;

import android.content.Intent;
import android.os.Bundle;

import com.jeeva.android.InAppActivity;

import in.sportscafe.scgame.module.analytics.ScGameAnalytics;

/**
 * Created by Jeeva on 6/4/16.
 */
public class ScGameActivity extends InAppActivity {

    @Override
    protected void onStart() {
        super.onStart();
        ScGameAnalytics.getInstance().onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ScGameAnalytics.getInstance().onStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScGameAnalytics.getInstance().onResume(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ScGameAnalytics.getInstance().onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ScGameAnalytics.getInstance().onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ScGameAnalytics.getInstance().onNewIntent(this, intent);
    }
}