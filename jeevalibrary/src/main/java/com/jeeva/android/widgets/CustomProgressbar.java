package com.jeeva.android.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jeeva.android.R;


public class CustomProgressbar extends Dialog {

    private static CustomProgressbar customProgressbar;

    private String mMessage;

    public CustomProgressbar(Context context) {
        super(context);
        mMessage = null;
       createProgressBar(context);
    }

    public CustomProgressbar(Context context, String message) {
        super(context);
        mMessage = message;
//        createProgressBar(context);
    }

    public synchronized static CustomProgressbar getProgressbar(Context context) {
        if (null == customProgressbar) {
            customProgressbar = new CustomProgressbar(context);
        }
        return customProgressbar;
    }

    public static CustomProgressbar getProgressbar(Context context, String message) {
        if (null == customProgressbar) {
            customProgressbar = new CustomProgressbar(context, message);
        }
        return customProgressbar;
    }

    /*public static View getProgressbarInView(Context context) {
        ProgressBar progressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleSmall);
        progressBar.setBackgroundResource(R.drawable.custom_circle_progress_bar);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        progressBar.setVisibility(View.VISIBLE);

        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setBackground(context.getResources().getDrawable(android.R.drawable.screen_background_dark_transparent));
        layout.setGravity(Gravity.CENTER);
        layout.addView(progressBar);
        layout.bringToFront();
        return layout;
    }*/

    private void createProgressBar(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getAppProgressbar(context));
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private View getAppProgressbar(Context context) {
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setBackgroundResource(android.R.color.transparent);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.loading_animation_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        if (null != mMessage) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(progressBar);
            TextView messageTextView = new TextView(context);
            messageTextView.setText(mMessage);
            messageTextView.setGravity(Gravity.CENTER_VERTICAL);
            messageTextView.setTypeface(Typeface.DEFAULT_BOLD);
  //          messageTextView.setTextColor(context.getResources().getColor(R.color.theme_yellow));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            messageTextView.setLayoutParams(layoutParams);
            layoutParams.setMargins(10, 0, 0, 0);
            linearLayout.addView(messageTextView);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            return linearLayout;
        }
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
//        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.lime_yellow), android.graphics.PorterDuff.Mode.MULTIPLY);
        return progressBar;
    }

    public boolean dismissProgress() {
        boolean dismissed = false;
        try {
            super.dismiss();
            dismissed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        customProgressbar = null;
        return dismissed;
    }
}