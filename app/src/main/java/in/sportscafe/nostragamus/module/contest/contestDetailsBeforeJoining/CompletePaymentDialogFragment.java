package in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.PopUpDialogFragment;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.store.dto.StoreItems;

/**
 * Created by deepanshi on 6/20/17.
 */

public class CompletePaymentDialogFragment extends PopUpDialogFragment implements View.OnClickListener {

    public interface DialogLaunchMode {
        int JOINING_CHALLENGE_LAUNCH = 1;
        int JOINING_CHALLENGE_AFTER_LOW_BAL_LAUNCH = 2;
        int STORE_BUY_POWERUP_LAUNCH = 3;
        int STORE_BUY_POWERUP_AFTER_LOW_BAL_LAUNCH = 4;
    }

    public interface CompletePaymentActionListener {
        void onBackClicked();
        void onPayConfirmed();
    }

    private CompletePaymentActionListener mCompletePaymentActionListener;
    private int mDialogRequestCode;
    private int mDialogLaunchMode;

    public void setSuccessListener(CompletePaymentActionListener listener) {
        mCompletePaymentActionListener = listener;
    }

    public void setDialogRequestCode(int requestCode) {
        this.mDialogRequestCode = requestCode;
    }

    public void setDialogLaunchMode(int launchMode) {
        this.mDialogLaunchMode = launchMode;
    }

    public static CompletePaymentDialogFragment newInstance(int requestCode, int dialogLaunchMode, Bundle args,
                                                            CompletePaymentActionListener listener) {
        CompletePaymentDialogFragment fragment = new CompletePaymentDialogFragment();
        fragment.setDialogRequestCode(requestCode);
        fragment.setDialogLaunchMode(dialogLaunchMode);
        fragment.setSuccessListener(listener);
        fragment.setArguments(args);
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
        populateValues();
    }

    private void initViews() {
        findViewById(R.id.complete_payment_btn_back).setOnClickListener(this);
        findViewById(R.id.complete_payment_btn).setOnClickListener(this);
        findViewById(R.id.popup_bg).setOnClickListener(this);
        ImageView mBtnPopupClose = (ImageView) findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);
    }

    private void populateValues() {
        switch (mDialogLaunchMode) {
            case DialogLaunchMode.JOINING_CHALLENGE_LAUNCH:
            case DialogLaunchMode.JOINING_CHALLENGE_AFTER_LOW_BAL_LAUNCH:
                populateChallengeJoiningValues();

                break;

            case DialogLaunchMode.STORE_BUY_POWERUP_LAUNCH:
            case DialogLaunchMode.STORE_BUY_POWERUP_AFTER_LOW_BAL_LAUNCH:
                populateStorePowerupBuyValues();
                break;

            default:
                break;
        }
    }

    private void populateStorePowerupBuyValues() {
        if (getView() != null) {

            /* Change button to buy */
            Button confirmButton = (Button) getView().findViewById(R.id.complete_payment_btn);
            confirmButton.setText("Buy");

            populateWalletBalance();

            Bundle bundle = getArguments();
            if (bundle != null) {
                StoreItems storeItems = Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.STORE_ITEM));
                if (storeItems != null) {

                    TextView entryFeeLabelTextView = (TextView) getView().findViewById(R.id.complete_payment_tv_entry_fee_txt);
                    if (!TextUtils.isEmpty(storeItems.getProductName())) {
                        entryFeeLabelTextView.setText(storeItems.getProductName() + " Price");
                    } else {
                        entryFeeLabelTextView.setText("Powerup Price");
                    }

                    /* Fee / price */
                    int price = storeItems.getProductPrice();
                    if (storeItems.getProductSaleInfo() != null && storeItems.getProductSaleInfo().getSaleOn()) {
                        price = storeItems.getProductSaleInfo().getSalePrice();
                    }
                    if (price > 0) {
                        populateEntryFee(price);
                    }

                    String string = "buy " + storeItems.getProductName();
                    populateWalletMoneyDeductionMsg(price, string);
                }

                if (mDialogLaunchMode == DialogLaunchMode.STORE_BUY_POWERUP_AFTER_LOW_BAL_LAUNCH) {
                    TextView balAddedTextView = (TextView) findViewById(R.id.join_challenge_dialog_money_added_textView);
                    String str = "Updated wallet balance is " + WalletHelper.getFormattedStringOfAmount(WalletHelper.getTotalBalance());
                    balAddedTextView.setText(str);
                    balAddedTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void populateChallengeJoiningValues() {
        if (getView() != null && getArguments() != null) {
            Bundle bundle = getArguments();
            JoinContestData joinContestData = Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));
            if (joinContestData != null) {

                // Header
                if (!TextUtils.isEmpty(joinContestData.getChallengeName())) {
                    TextView headerTextView = (TextView) findViewById(R.id.join_challenge_dialog_header_textView);
                    String str = "Join " + joinContestData.getChallengeName() + " Contest";
                    headerTextView.setText(str);

                    populateWalletMoneyDeductionMsg(joinContestData.getEntryFee(), str);
                }
                populateWalletBalance();
                populateEntryFee(joinContestData.getEntryFee());

                // Dialog launch mode
                switch (joinContestData.getJoiContestDialogLaunchMode()) {
                    case DialogLaunchMode.JOINING_CHALLENGE_AFTER_LOW_BAL_LAUNCH:
                        TextView balAddedTextView = (TextView) findViewById(R.id.join_challenge_dialog_money_added_textView);
                        String str = "Updated wallet balance is " + WalletHelper.getFormattedStringOfAmount(WalletHelper.getTotalBalance());
                        balAddedTextView.setText(str);
                        balAddedTextView.setVisibility(View.VISIBLE);
                        break;

                    case DialogLaunchMode.JOINING_CHALLENGE_LAUNCH:
                        break;
                }

            }
        }
    }

    private void populateWalletMoneyDeductionMsg(double price, String operationStr) {
        TextView msgTextView = (TextView) findViewById(R.id.join_challenge_dialog_msg_textView);
        String msg = WalletHelper.getFormattedStringOfAmount(price) + " will be deducted from your wallet to " + operationStr;
        msgTextView.setText(msg);
    }

    private void populateEntryFee(double entryFee) {
        TextView tvEntryFee = (TextView) findViewById(R.id.complete_payment_tv_entry_fee);
        tvEntryFee.setText(WalletHelper.getFormattedStringOfAmount(entryFee));
    }

    private void populateWalletBalance() {
        TextView tvBalanceAmount = (TextView) findViewById(R.id.complete_payment_tv_wallet_balance);
        double walletBalAmount = WalletHelper.getTotalBalance();
        tvBalanceAmount.setText(WalletHelper.getFormattedStringOfAmount(walletBalAmount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete_payment_btn_back:
            case R.id.popup_cross_btn:
                if (mCompletePaymentActionListener != null) {
                    mCompletePaymentActionListener.onBackClicked();
                }
                dismiss();
                break;

            case R.id.popup_bg:
                if (mCompletePaymentActionListener != null) {
                    mCompletePaymentActionListener.onBackClicked();
                }
                dismiss();
                break;

            case R.id.complete_payment_btn:
                if (mCompletePaymentActionListener != null) {
                    mCompletePaymentActionListener.onPayConfirmed();
                }
                dismiss();
                break;
        }
    }
}