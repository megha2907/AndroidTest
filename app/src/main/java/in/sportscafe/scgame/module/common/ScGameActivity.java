package in.sportscafe.scgame.module.common;

import android.content.Intent;
import android.os.Bundle;

import com.jeeva.android.InAppActivity;
import com.moe.pushlibrary.MoEHelper;

/**
 * Created by Jeeva on 6/4/16.
 */
public class ScGameActivity extends InAppActivity {

    private MoEHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = MoEHelper.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.onStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper.onResume(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mHelper.onNewIntent(this, intent);
    }

    public MoEHelper getMoeHelper() {
        return mHelper;
    }
}