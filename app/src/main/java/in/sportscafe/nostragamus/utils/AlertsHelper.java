package in.sportscafe.nostragamus.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by sandip on 19/09/17.
 */

public class AlertsHelper {

    public static void showAlert(Context context, String title, String message,
                                 DialogInterface.OnClickListener clickListener) {
        if (clickListener == null) {
            clickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(android.R.string.ok, clickListener);
        dialogBuilder.show();
    }

}
