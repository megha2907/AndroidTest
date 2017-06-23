package in.sportscafe.nostragamus.module.allchallenges.join;

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
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

/**
 * Created by deepanshi on 6/20/17.
 */

public class CompletePaymentAndJoinDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    public interface CompletePaymentActionListener {
        void onBackClicked();
        void onPayAndJoin();
    }

    private CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener mCompletePaymentActionListener;
    private int mDialogRequestCode;
    private double mEntryFee;

    public void setSuccessListener(CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener listener) {
        mCompletePaymentActionListener = listener;
    }

    public static CompletePaymentAndJoinDialogFragment newInstance(int requestCode, double entryFee,
                                                                   CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putDouble(Constants.BundleKeys.ENTRY_FEE, entryFee);

        CompletePaymentAndJoinDialogFragment fragment = new CompletePaymentAndJoinDialogFragment();
        fragment.setSuccessListener(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complete_payment_dialog, container, false);
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
        mEntryFee = bundle.getDouble(Constants.BundleKeys.ENTRY_FEE);
    }

    private void initViews() {
        findViewById(R.id.complete_payment_btn_back).setOnClickListener(this);
        findViewById(R.id.complete_payment_btn_pay_join).setOnClickListener(this);
        ImageView mBtnPopupClose = (ImageView) findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);
    }

    private void initValues() {
        if (getView() != null) {
            TextView tvBalanceAmount = (TextView) findViewById(R.id.complete_payment_tv_wallet_balance);
            double walletBalAmount = WalletHelper.getTotalBalance();
            tvBalanceAmount.setText(WalletHelper.getFormattedStringOfAmount(walletBalAmount));

            TextView tvEntryFee = (TextView) findViewById(R.id.complete_payment_tv_entry_fee);
            tvEntryFee.setText(WalletHelper.getFormattedStringOfAmount(mEntryFee));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete_payment_btn_back:
                if (mCompletePaymentActionListener != null) {
                    mCompletePaymentActionListener.onBackClicked();
                }
                dismiss();
                break;

            case R.id.popup_cross_btn:
                if (mCompletePaymentActionListener != null) {
                    mCompletePaymentActionListener.onBackClicked();
                }
                dismiss();
                break;
            case R.id.complete_payment_btn_pay_join:
                if (mCompletePaymentActionListener != null) {
                    mCompletePaymentActionListener.onPayAndJoin();
                }
                dismiss();
                break;
        }
    }
}