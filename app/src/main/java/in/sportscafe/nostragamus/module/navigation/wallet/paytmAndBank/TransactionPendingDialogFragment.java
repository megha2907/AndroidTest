package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * Created by deepanshi on 3/10/18.
 */

public class TransactionPendingDialogFragment extends PopUpDialogFragment implements View.OnClickListener {

    public interface IPendingActionListener {
        void onBackToWallet();
    }

    private TransactionPendingDialogFragment.IPendingActionListener mPendingActionListener;
    private int mDialogRequestCode;
    private double mTransactionAmount;

    public void setPendingListener(TransactionPendingDialogFragment.IPendingActionListener listener) {
        mPendingActionListener = listener;
    }

    public static TransactionPendingDialogFragment newInstance(int requestCode, double amount,
                                                               TransactionPendingDialogFragment.IPendingActionListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putDouble(Constants.BundleKeys.TRANSACTION_AMOUNT, amount);

        TransactionPendingDialogFragment fragment = new TransactionPendingDialogFragment();
        fragment.setPendingListener(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_pending_dialog, container, false);
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
        findViewById(R.id.back_to_wallet_button).setOnClickListener(this);
        findViewById(R.id.popup_bg).setOnClickListener(this);
        ImageView mBtnPopupClose = (ImageView)findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);
    }

    private void initValues() {

        TextView tvTransactionPending = (TextView) findViewById(R.id.pending_payment_Textview);
        if (mTransactionAmount != 0) {
            tvTransactionPending.setText(WalletHelper.getFormattedStringOfAmount(mTransactionAmount) +
                    " will get added to your wallet. We will notify you once the transaction is complete");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_wallet_button:
            case R.id.popup_bg:
            case R.id.popup_cross_btn:
                if (mPendingActionListener != null) {
                    mPendingActionListener.onBackToWallet();
                }
                dismiss();
                break;
        }
    }
}
