package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.PopUpDialogFragment;
import in.sportscafe.nostragamus.module.common.dto.Challenge;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFailureDialogFragment extends PopUpDialogFragment implements View.OnClickListener {

    public interface IFailureActionListener {
        void onBackToSelectPaymentMode();
        void onRetryPayment();
    }

    private IFailureActionListener mPaytmFailureListener;
    private int mDialogRequestCode;
    private Challenge mChallenge;

    public void setFailureListener(IFailureActionListener listener) {
        mPaytmFailureListener = listener;
    }

    public static TransactionFailureDialogFragment newInstance(int requestCode,
                                                               IFailureActionListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);

        TransactionFailureDialogFragment fragment = new TransactionFailureDialogFragment();
        fragment.setFailureListener(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paytm_transaction_failure_dialog, container, false);
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
    }

    private void openBundle(Bundle bundle) {
        mDialogRequestCode = bundle.getInt(Constants.BundleKeys.DIALOG_REQUEST_CODE);
    }

    private void initViews() {
        findViewById(R.id.paytm_failure_backToAddMoney_button).setOnClickListener(this);
        findViewById(R.id.paytm_failure_retry_payment_button).setOnClickListener(this);
        findViewById(R.id.popup_bg).setOnClickListener(this);
        ImageView mBtnPopupClose = (ImageView)findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paytm_failure_backToAddMoney_button:
                if (mPaytmFailureListener != null) {
                    mPaytmFailureListener.onBackToSelectPaymentMode();
                }
                dismiss();
                break;

            case R.id.paytm_failure_retry_payment_button:
                if (mPaytmFailureListener != null) {
                    mPaytmFailureListener.onRetryPayment();
                }
                dismiss();
                break;

            case R.id.popup_cross_btn:
                dismiss();
                break;

            case R.id.popup_bg:
                dismiss();
                break;
        }
    }
}
