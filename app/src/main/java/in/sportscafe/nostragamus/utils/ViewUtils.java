package in.sportscafe.nostragamus.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by Jeeva on 17/6/16.
 */
public class ViewUtils {

    static class DialogListAdapter extends ArrayAdapter<String> implements ListAdapter {

        public DialogListAdapter(Context context, List<String> options) {
            super(context, android.R.layout.simple_list_item_1, options);
        }
    }

    public static AlertDialog getDialogList(Context context, List<String> options,
                                            DialogInterface.OnClickListener clickListener) {
        return new AlertDialog.Builder(context)
                .setAdapter(new DialogListAdapter(context, options), clickListener)
                .create();
    }

    public static AlertDialog getAlertDialog(Context context, String message,
                                             String positiveButton,
                                             DialogInterface.OnClickListener positiveListener,
                                             String negativeButton,
                                             DialogInterface.OnClickListener negativeListener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveListener)
                .setNegativeButton(negativeButton, negativeListener)
                .create();
    }
}