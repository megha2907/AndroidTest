package com.jeeva.android;

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
public class InAppFragment extends BaseFragment {

    private RelativeLayout mRlInApp;

    private ProgressBar mProgressBar;

    private LinearLayout mLlMessage;

    private TextView mTvMessage;

    private Button mBtnPositive;

    private Button mBtnNegative;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRlInApp = (RelativeLayout) getLayoutInflater().inflate(R.layout.inflater_inapp, null);

        mProgressBar = (ProgressBar) mRlInApp.findViewById(R.id.inflater_inapp_pb);

        mLlMessage = (LinearLayout) mRlInApp.findViewById(R.id.inflater_inapp_ll_message);

        mTvMessage = (TextView) mRlInApp.findViewById(R.id.inflater_inapp_tv_message);

        mBtnPositive = (Button) mRlInApp.findViewById(R.id.inflater_inapp_btn_positive);

        mBtnNegative = (Button) mRlInApp.findViewById(R.id.inflater_inapp_btn_negative);

        ((ViewGroup) getView()).addView(mRlInApp, new RelativeLayout.LayoutParams(
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

        mBtnPositive.setVisibility(View.VISIBLE);
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
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.bright_turquoise), android.graphics.PorterDuff.Mode.MULTIPLY);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dismissProgressbar() {
        boolean dismissed = false;
        if(null != getContext()) {
            try {
                hideInApp();
                mProgressBar.setVisibility(View.GONE);
                dismissed = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dismissed;
    }

    public void showInApp() {
        mRlInApp.setVisibility(View.VISIBLE);
    }

    public void hideInApp() {
        mRlInApp.setVisibility(View.GONE);
    }
}