package com.jeeva.android;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.CustomToast;

import java.util.List;


/**
 * Created by Jeeva on 16/10/14.
 */
public abstract class BaseActivity extends AppCompatActivity implements com.jeeva.android.View {

    public FragmentActivity getActivity() {
        return BaseActivity.this;
    }

    public Context getContext(){
        return BaseActivity.this;
    }

    public void showMessage(String message) {
//        CustomToast.getToast(BaseActivity.this).showSnack(findViewById(android.R.id.content), message);
        CustomToast.getToast(BaseActivity.this).show(message);
    }

    public void showMessage(String message, int duration) {
//        CustomToast.getToast(BaseActivity.this).showSnack(findViewById(android.R.id.content), message, duration);
        CustomToast.getToast(BaseActivity.this).show(message, duration);
    }

    @Override
    public boolean isMessageShowing() {
//        return CustomToast.getToast(BaseActivity.this).isSnackShowing();
        return CustomToast.getToast(BaseActivity.this).isToastShowing();
    }

    public void showProgressbar() {
        CustomProgressbar.getProgressbar(BaseActivity.this).show();
    }

    public void updateProgressbar() {
        // TODO update the progressbar
    }

    public void dismissProgressbar() {
        CustomProgressbar.getProgressbar(BaseActivity.this).dismiss();
    }

    public void showSoftKeyboard(View view) {
        getInputMethodManager().showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard() {
        if (null != getCurrentFocus())
            getInputMethodManager().hideSoftInputFromWindow(getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public String getTrimmedText(TextView textView) {
        return textView.getText().toString().trim();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if(null != fragment) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

}