package in.sportscafe.nostragamus.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by sandip on 28/08/17.
 */

public class NostraProgressDialog {

    private static NostraProgressDialog sNostraProgressDialog;
    private static ProgressDialog sProgressBar;

    private NostraProgressDialog(Context context) {
        sProgressBar = new android.app.ProgressDialog(context);
    }

    public static synchronized NostraProgressDialog getInstance(Context context) {
        if (sNostraProgressDialog == null) {
            sNostraProgressDialog = new NostraProgressDialog(context);
        }
        return sNostraProgressDialog;
    }

    public void showProgress() {
        if (sProgressBar != null) {
            sProgressBar.show();
        }
    }

    public void dismissProgress() {
        if (sProgressBar != null && sProgressBar.isShowing()) {
            sProgressBar.dismiss();
        }
    }
}
