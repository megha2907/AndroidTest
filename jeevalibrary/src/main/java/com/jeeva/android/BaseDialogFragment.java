package com.jeeva.android;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jeeva on 16/10/14.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    public Context getContext(){
        return getActivity();
    }

    public void showMessage(String message) {
        View view = getActivityView();
        if (null != view) {
            view.showMessage(message);
        }
    }

    public void showMessage(String message, int duration) {
        View view = getActivityView();
        if (null != view) {
            view.showMessage(message, duration);
        }
    }

    public boolean isMessageShowing() {
        View view = getActivityView();
        if (null != view) {
            return view.isMessageShowing();
        }
        return false;
    }

    public void showProgressbar() {
        getActivityView().showProgressbar();
    }

    public void updateProgressbar() {
    }

    public boolean dismissProgressbar() {
        if(getActivityView() != null) {
            return getActivityView().dismissProgressbar();
        }
        return false;
    }

    public void showSoftKeyboard(android.view.View view) {
        getActivityView().showSoftKeyboard(view);
    }

    public void hideSoftKeyboard() {
        getActivityView().hideSoftKeyboard();
    }

    public LayoutInflater getLayoutInflater() {
        return getActivityView().getLayoutInflater();
    }

    public String getTrimmedText(TextView textView) {
        return getActivityView().getTrimmedText(textView);
    }

    private View getActivityView() {
        return (View) getActivity();
    }

    public android.view.View findViewById(int id) {
        return getView().findViewById(id);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getParentFragment().startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}