package com.jeeva.android;

/**
 * Created by Jeeva on 16/10/14.
 */
public interface InAppView extends View {

    void showInApp();

    void hideInApp();

    void showInAppMessage(String message);

    void showMessage(String message, String positiveButton, android.view.View.OnClickListener positiveClickListener);

    void showMessage(String message, String positiveButton, android.view.View.OnClickListener positiveClickListener,
                     String negativeButton, android.view.View.OnClickListener negativeClickListener);
}