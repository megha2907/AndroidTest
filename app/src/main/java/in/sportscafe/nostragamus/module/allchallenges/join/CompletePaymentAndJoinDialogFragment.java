package in.sportscafe.nostragamus.module.allchallenges.join;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
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

    public interface DialogLaunchFlow {
        int NORMAL_LAUNCH = 1;
        int MONEY_ADDED_ON_LOW_BAL_LAUNCH = 2;
    }

    public interface CompletePaymentActionListener {
        void onBackClicked();
        void onPayAndJoin();
    }

    private CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener mCompletePaymentActionListener;
    private int mDialogRequestCode;

    public void setSuccessListener(CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener listener) {
        mCompletePaymentActionListener = listener;
    }

    public static CompletePaymentAndJoinDialogFragment newInstance(int requestCode, String configName, double entryFee, int dialogLaunchMode,
                                                                   CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putDouble(Constants.BundleKeys.ENTRY_FEE, entryFee);
        bundle.putString(Constants.BundleKeys.CONFIG_NAME, configName);
        bundle.putInt(Constants.BundleKeys.DIALOG_LAUNCH_MODE, dialogLaunchMode);

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
        initViews();
        initValues();
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
            Bundle bundle = getArguments();

            if (bundle != null) {
                mDialogRequestCode = bundle.getInt(Constants.BundleKeys.DIALOG_REQUEST_CODE);
                double entryFee = bundle.getDouble(Constants.BundleKeys.ENTRY_FEE);
                String configName = bundle.getString(Constants.BundleKeys.CONFIG_NAME);
                int dialogLaunchMode = bundle.getInt(Constants.BundleKeys.DIALOG_LAUNCH_MODE);

                // Header
                if (!TextUtils.isEmpty(configName)) {
                    TextView headerTextView = (TextView) findViewById(R.id.join_challenge_dialog_header_textView);
                    String str = "Join " + configName + " Contest";
                    headerTextView.setText(str);
                }

                // balance
                TextView tvBalanceAmount = (TextView) findViewById(R.id.complete_payment_tv_wallet_balance);
                double walletBalAmount = WalletHelper.getTotalBalance();
                tvBalanceAmount.setText(WalletHelper.getFormattedStringOfAmount(walletBalAmount));

                // entry fee
                TextView tvEntryFee = (TextView) findViewById(R.id.complete_payment_tv_entry_fee);
                tvEntryFee.setText(WalletHelper.getFormattedStringOfAmount(entryFee));

                // contest entry msg
                TextView msgTextView = (TextView) findViewById(R.id.join_challenge_dialog_msg_textView);
                String msg = WalletHelper.getFormattedStringOfAmount(entryFee) + " will be deducted from your wallet to join this contest";
                msgTextView.setText(msg);

                // Dialog launch mode
                if (dialogLaunchMode == DialogLaunchFlow.MONEY_ADDED_ON_LOW_BAL_LAUNCH) {
                    TextView balAddedTextView = (TextView) findViewById(R.id.join_challenge_dialog_money_added_textView);
                    String str = "Updated wallet balance is " + WalletHelper.getFormattedStringOfAmount(entryFee);
                    balAddedTextView.setText(str);
                    balAddedTextView.setVisibility(View.VISIBLE);
                }
            }
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