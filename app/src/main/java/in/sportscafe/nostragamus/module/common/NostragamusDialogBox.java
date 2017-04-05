package in.sportscafe.nostragamus.module.common;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;

/**
 * Created by Jeeva on 13/12/16.
 */
public class NostragamusDialogBox extends AlertDialog {

    private EditText mEtInputBox;

    public NostragamusDialogBox(Context context) {
        super(context);

        initView(context);
    }

    private void initView(Context context) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
        setContentView(convertView);
        setView(convertView);

        mEtInputBox = (EditText) findViewById(R.id.dialog_et_input);
    }

    public NostragamusDialogBox title(int title) {
        TextView textView = ((TextView) findViewById(R.id.dialog_tv_title));
        textView.setVisibility(View.VISIBLE);
        textView.setText(title);
        return this;
    }

    public NostragamusDialogBox message(int message) {
        return message(getContext().getString(message));
    }

    public NostragamusDialogBox message(String message) {
        TextView textView = ((TextView) findViewById(R.id.dialog_tv_message));
        textView.setVisibility(View.VISIBLE);
        textView.setText(AppSnippet.formatHtml(message));
        return this;
    }

    public NostragamusDialogBox positiveButton(int positiveButton, View.OnClickListener positiveListener) {
        Button button = (Button) findViewById(R.id.dialog_btn_positive);
        button.setVisibility(View.VISIBLE);
        button.setText(positiveButton);
        button.setOnClickListener(positiveListener);
        return this;
    }

    public NostragamusDialogBox negativeButton(int negativeButton, View.OnClickListener negativeListener) {
        Button button = (Button) findViewById(R.id.dialog_btn_negative);
        button.setVisibility(View.VISIBLE);
        button.setText(negativeButton);
        button.setOnClickListener(negativeListener);
        return this;
    }

    public NostragamusDialogBox dontShowCheck(CompoundButton.OnCheckedChangeListener dontShowListener) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.dialog_tv_show_again);
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setOnCheckedChangeListener(dontShowListener);
        return this;
    }

    public NostragamusDialogBox inputBox(int hint) {
        mEtInputBox.setVisibility(View.VISIBLE);
        mEtInputBox.setHint(hint);
        return this;
    }

    public String getInputBoxText() {
        return mEtInputBox.getText().toString();
    }
}