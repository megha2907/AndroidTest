package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.PopUpDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.PayoutChoiceType;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawInitiatedDialogFragment extends PopUpDialogFragment implements View.OnClickListener {

    private static final String TAG = WithdrawInitiatedDialogFragment.class.getSimpleName();

    private WithdrawApiDialogListener mDialogListener;

    public WithdrawInitiatedDialogFragment() {}

    public void setDialogListener (WithdrawApiDialogListener listener) {
        mDialogListener = listener;
    }

    public static WithdrawInitiatedDialogFragment newInstance(WithdrawApiDialogListener dialogListener) {
        WithdrawInitiatedDialogFragment fragment = new WithdrawInitiatedDialogFragment();
        fragment.setDialogListener(dialogListener);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_withdraw_initiated_dialog, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        rootView.findViewById(R.id.withdraw_dialog_ok_button).setOnClickListener(this);
        ImageView mBtnPopupClose = (ImageView) rootView.findViewById(R.id.popup_cross_btn);
        rootView.findViewById(R.id.popup_bg).setOnClickListener(this);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    private void initialize() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.WITHDRAW_STATUS_CODE)) {
            int statusCode = args.getInt(Constants.BundleKeys.WITHDRAW_STATUS_CODE, -1);
            double amount = args.getDouble(Constants.BundleKeys.WITHDRAW_AMOUNT, -1);
            String payoutType = args.getString(Constants.BundleKeys.WITHDRAW_PAYOUT_TYPE, "");

            View view = getView();
            if (view != null) {
                TextView headerTextView = (TextView) view.findViewById(R.id.withdraw_dialog_header_textView);
                TextView statusTextView = (TextView) view.findViewById(R.id.withdraw_dialog_status_textView);
                TextView statusMsgTextView = (TextView) view.findViewById(R.id.withdraw_dialog_status_msg_textView);
                ImageView statusImageView = (ImageView) view.findViewById(R.id.withdraw_dialog_imgView);

                switch (statusCode) {
                    case Constants.WithdrawFromWalletResponseCode.SUCCESS:
                        initSuccessDialog(amount, payoutType, headerTextView, statusTextView, statusMsgTextView, statusImageView);
                        break;

                    case Constants.WithdrawFromWalletResponseCode.ERROR_INSUFICIENT_BALANCE:
                    case Constants.WithdrawFromWalletResponseCode.ERROR_MIN_BALANCE_REQUIRED:
                    case Constants.WithdrawFromWalletResponseCode.ERROR_UNKNOWN:
                        initFailureDialog(amount, payoutType, headerTextView, statusTextView, statusMsgTextView, statusImageView);
                        break;
                }
            }
        }
    }

    private void initFailureDialog(double amount, String payoutType,
                                   TextView headerTextView, TextView statusTextView,
                                   TextView statusMsgTextView, ImageView statusImageView) {

        headerTextView.setText("Transaction Failed");
        statusTextView.setText("Transaction Initiation Failed");
        statusMsgTextView.setText("The provider declined the payment.");
        statusImageView.setImageResource(R.drawable.wallet_withdraw_failed_icn);

    }

    private void initSuccessDialog(double amount, String payoutType,
                                   TextView headerTextView, TextView statusTextView,
                                   TextView statusMsgTextView, ImageView statusImageView) {

        String msg = "";
        if (amount > 0) {
            String amtStr = WalletHelper.getFormattedStringOfAmount(amount);
            String payout = "";

            if (payoutType.equals(PayoutChoiceType.PAYTM)) {
                payout = "PayTm wallet ";
            } else if (payoutType.equals(PayoutChoiceType.BANK)) {
                payout = "bank ";
            }

            if (!TextUtils.isEmpty(payout)) {
                msg = amtStr + " will be added to your " + payout + " in the next 24 - 48 hours";
            }
        }

        headerTextView.setText("Transaction Started");
        statusTextView.setText("Transaction Initiated");
        statusMsgTextView.setText(msg);
        statusImageView.setImageResource(R.drawable.wallet_withdraw_initiated_icn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdraw_dialog_ok_button:
            case R.id.popup_cross_btn:
                onOkButtonClicked();
                break;
            case R.id.popup_bg:
                onOkButtonClicked();
                break;
        }
    }

    private void onOkButtonClicked() {
        if (mDialogListener != null) {
            mDialogListener.onOkClicked();
        }
    }
}
