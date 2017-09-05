package in.sportscafe.nostragamus.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.Contest;

/**
 * Created by sandip on 28/08/17.
 */

public class NostraProgressDialog extends ProgressDialog {

    private static NostraProgressDialog sNostraProgressDialog;
    private ProgressDialog progressDialog;

    public NostraProgressDialog(Context context) {
        super(context);
    }

    public static synchronized NostraProgressDialog getInstance(Context context) {
        if (sNostraProgressDialog == null) {
            sNostraProgressDialog = new NostraProgressDialog(context);
        }
        return sNostraProgressDialog;
    }

    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this.getContext(), R.style.NostraProgressDialog) {
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.nostra_progress_dialog);
                }
            };
        }
        progressDialog.show();
    }

    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
