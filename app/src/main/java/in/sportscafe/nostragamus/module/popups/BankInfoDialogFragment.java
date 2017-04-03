package in.sportscafe.nostragamus.module.popups;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;

/**
 * Created by Jeeva on 28/02/17.
 */
public class BankInfoDialogFragment extends NostragamusDialogFragment {

    private OnDismissListener mDismissListener;

    public static BankInfoDialogFragment newInstance(int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);

        BankInfoDialogFragment fragment = new BankInfoDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDismissListener) {
            mDismissListener = (OnDismissListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setBankTransferIcon();

        getView().findViewById(R.id.bank_info_btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setBankTransferIcon() {
        TextView textView = (TextView) findViewById(R.id.bank_info_tv_transfer_text);
        String text = textView.getText().toString();
        String replaceText = "tranfer_icon";


        SpannableString spannableString = new SpannableString(text);
        Drawable d = getResources().getDrawable(R.drawable.bank_transfer_info_icon);
        d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
        ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, text.indexOf(replaceText), text.indexOf(replaceText) + replaceText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDismissListener.onDismiss(getArguments().getInt(BundleKeys.DIALOG_REQUEST_CODE), null);
    }
}