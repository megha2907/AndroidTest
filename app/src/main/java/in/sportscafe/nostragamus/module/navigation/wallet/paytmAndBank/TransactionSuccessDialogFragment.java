package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.PopUpDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionSuccessDialogFragment extends PopUpDialogFragment implements View.OnClickListener {

    public interface ISuccessActionListener {
        void onBackToHomeClicked();
    }

    private ISuccessActionListener mPaytmFailureListener;
    private int mDialogRequestCode;
    private double mTransactionAmount;

    public void setSuccessListener(ISuccessActionListener listener) {
        mPaytmFailureListener = listener;
    }

    public static TransactionSuccessDialogFragment newInstance(int requestCode, double amount,
                                                               ISuccessActionListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putDouble(Constants.BundleKeys.TRANSACTION_AMOUNT, amount);

        TransactionSuccessDialogFragment fragment = new TransactionSuccessDialogFragment();
        fragment.setSuccessListener(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_success_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCancelable(false);
        openBundle(getArguments());
        initViews();
        initValues();
    }

    private void openBundle(Bundle bundle) {
        mDialogRequestCode = bundle.getInt(Constants.BundleKeys.DIALOG_REQUEST_CODE);
        mTransactionAmount = bundle.getDouble(Constants.BundleKeys.TRANSACTION_AMOUNT);
    }

    private void initViews() {
        findViewById(R.id.paytm_transaction_success_back_button).setOnClickListener(this);
        findViewById(R.id.popup_bg).setOnClickListener(this);
        ImageView mBtnPopupClose = (ImageView)findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);
    }

    private void initValues() {

        TextView tvPaytmTransactionSuccess = (TextView) findViewById(R.id.paytm_transaction_success_tv_desc);
        if (mTransactionAmount != 0) {
            tvPaytmTransactionSuccess.setText(WalletHelper.getFormattedStringOfAmount(mTransactionAmount) +
                    " has been successfully added to your wallet!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paytm_transaction_success_back_button:
                if (mPaytmFailureListener != null) {
                    mPaytmFailureListener.onBackToHomeClicked();
                }
                dismiss();
                break;

            case R.id.popup_cross_btn:
                if (mPaytmFailureListener != null) {
                    mPaytmFailureListener.onBackToHomeClicked();
                }
                dismiss();
                break;

            case R.id.popup_bg:
                if (mPaytmFailureListener != null) {
                    mPaytmFailureListener.onBackToHomeClicked();
                }
                dismiss();
                break;
        }
    }
}
