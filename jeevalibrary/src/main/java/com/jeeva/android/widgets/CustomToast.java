package com.jeeva.android.widgets;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jeeva.android.R;


public class CustomToast extends Toast {

    private static CustomToast customToast = null;

    private Snackbar mSnackbar;

    private CustomToast(Context context) {
        super(context);
        setGravity(Gravity.BOTTOM, 0, 30);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.inflater_custom_toast, null);
        setView(view);
    }

    public static CustomToast getToast(Context context) {
        if (null == customToast) {
            customToast = new CustomToast(context);
        }
        return customToast;
    }

    public void show(String message) {
        setText(message);
        show();
    }

    public void show(String message, int duration) {
        setDuration(duration);
        show(message);
    }

    public boolean isToastShowing() {
        return customToast.getView().isShown();
    }

    public void showSnack(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        mSnackbar.show();
    }

    public void showSnack(View view, String message, int duration) {
        mSnackbar = Snackbar.make(view, message, duration);
        mSnackbar.show();
    }

    public boolean isSnackShowing() {
        if(null != mSnackbar) {
            return mSnackbar.isShown();
        }
        return false;
    }
}