package com.jeeva.android;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Jeeva on 3/8/15.
 */
public class InAppActivity extends BaseActivity {

    private RelativeLayout mRlInApp;

    private ProgressBar mProgressBar;

    private LinearLayout mLlMessage;

    private TextView mTvMessage;

    private Button mBtnPositive;

    private Button mBtnNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mRlInApp = (RelativeLayout) getLayoutInflater().inflate(R.layout.inflater_inapp, null);

        mProgressBar = (ProgressBar) mRlInApp.findViewById(R.id.inflater_inapp_pb);

        mLlMessage = (LinearLayout) mRlInApp.findViewById(R.id.inflater_inapp_ll_message);

        mTvMessage = (TextView) mRlInApp.findViewById(R.id.inflater_inapp_tv_message);

        mBtnPositive = (Button) mRlInApp.findViewById(R.id.inflater_inapp_btn_positive);

        mBtnNegative = (Button) mRlInApp.findViewById(R.id.inflater_inapp_btn_negative);

        ((ViewGroup) findViewById(R.id.content)).addView(mRlInApp,
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void showInAppMessage(String message) {
        showInApp();

        mProgressBar.setVisibility(View.GONE);
        mLlMessage.setVisibility(View.VISIBLE);

        mTvMessage.setText(message);

        mBtnPositive.setVisibility(View.GONE);

        mBtnNegative.setVisibility(View.GONE);
    }

    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener) {
        showInApp();

        mProgressBar.setVisibility(View.GONE);
        mLlMessage.setVisibility(View.VISIBLE);

        mTvMessage.setText(message);

        mBtnPositive.setVisibility(View.VISIBLE);
        mBtnPositive.setText(positiveButton);
        mBtnPositive.setOnClickListener(positiveClickListener);

        mBtnNegative.setVisibility(View.GONE);
    }

    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener,
                            String negativeButton, View.OnClickListener negativeClickListener) {
        showInApp();

        mProgressBar.setVisibility(View.GONE);
        mLlMessage.setVisibility(View.VISIBLE);

        mTvMessage.setText(message);

        mBtnPositive.setText(positiveButton);
        mBtnPositive.setOnClickListener(positiveClickListener);

        mBtnNegative.setVisibility(View.VISIBLE);
        mBtnNegative.setText(negativeButton);
        mBtnNegative.setOnClickListener(negativeClickListener);
    }

    @Override
    public void showProgressbar() {
        showInApp();
        mLlMessage.setVisibility(View.GONE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getColor(getContext(), R.color.bright_turquoise), android.graphics.PorterDuff.Mode.MULTIPLY);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressbar() {
        hideInApp();
        mProgressBar.setVisibility(View.GONE);
    }

    public void showInApp() {
        mRlInApp.setVisibility(View.VISIBLE);
    }

    public void hideInApp() {
        mRlInApp.setVisibility(View.GONE);
    }

    public int getColor(Context context, int colorRes) {
        Resources resources = context.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColor(colorRes, null);
        }
        return resources.getColor(colorRes);
    }
}