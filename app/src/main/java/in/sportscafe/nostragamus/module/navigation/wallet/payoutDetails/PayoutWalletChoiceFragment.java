package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.WithdrawFromWalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto.WithdrawFromWalletResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayoutWalletChoiceFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = PayoutWalletChoiceFragment.class.getSimpleName();

    private PayoutWalletChoiceFragmentListener mFragmentListener;

    private RadioButton mPaytmRadioButton;
    private RadioButton mBankRadioButton;

    public PayoutWalletChoiceFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PayoutWalletChoiceFragmentListener) {
            mFragmentListener = (PayoutWalletChoiceFragmentListener) context;
        } else {
            throw  new RuntimeException("Activity must implement " +
                    PayoutWalletChoiceFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payout_wallet_choice, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.withdraw_bottom_button).setOnClickListener(this);
        rootView.findViewById(R.id.payout_choice_paytm_button).setOnClickListener(this);
        rootView.findViewById(R.id.payout_choice_bank_button).setOnClickListener(this);
        rootView.findViewById(R.id.payout_choice_no_account_button).setOnClickListener(this);

        mPaytmRadioButton = (RadioButton) rootView.findViewById(R.id.payout_choice_paytm_radio_button);
        mBankRadioButton = (RadioButton) rootView.findViewById(R.id.payout_choice_bank_radio_button);
        mPaytmRadioButton.setOnCheckedChangeListener(this);
        mBankRadioButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        showPayoutAccounts();
        initWithdrawalAmount();
    }

    private void showPayoutAccounts() {
        boolean isPaytmProvided = WalletHelper.isPaytmPayoutDetailsProvided();
        boolean isBankProvided = WalletHelper.isBankPayoutDetailsProvided();
        View view = getView();

        if (view != null) {
            if (isPaytmProvided) {
                view.findViewById(R.id.payout_choice_paytm_button).setVisibility(View.VISIBLE);
                mPaytmRadioButton.setChecked(true);
            }
            if (isBankProvided) {
                view.findViewById(R.id.payout_choice_bank_button).setVisibility(View.VISIBLE);
                if (mPaytmRadioButton.getVisibility() != View.VISIBLE) {
                    mBankRadioButton.setChecked(true);
                }
            }

            // If no account details available
            if (!isPaytmProvided && !isBankProvided) {
                view.findViewById(R.id.payout_choice_no_account_button).setVisibility(View.VISIBLE);
                view.findViewById(R.id.withdraw_bottom_button).setEnabled(false);
            }
        }
    }

    /**
     * If user was directed to add payout details in case of no payout-account provided, once the details are added;
     * Fetch and show payout details again
     */
    public void onPayoutDetailsAdded() {
        fetchWalletDetails();
    }

    private void fetchWalletDetails() {
        showProgressbar();
        WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(UserWalletResponse response) {
                dismissProgressbar();
                showPayoutAccounts();
            }
        }).performApiCall();
    }

    private void initWithdrawalAmount() {
        double amount = getWithdrawalAmount();
    }

    private double getWithdrawalAmount() {
        double amount = 0;
        Bundle args = getArguments();
        if (args != null) {
            amount = args.getDouble(Constants.BundleKeys.WALLET_WITHDRAWAL_AMT, -1);
        }
        return amount;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdraw_bottom_button:
                onWithdrawClicked();
                break;

            case R.id.payout_choice_paytm_button:
                mPaytmRadioButton.setChecked(true);
                break;

            case R.id.payout_choice_bank_button:
                mBankRadioButton.setChecked(true);
                break;

            case R.id.payout_choice_no_account_button:
                onAddPayoutButtonClicked();
                break;
        }
    }

    private void onAddPayoutButtonClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onAddPayoutDetailsClicked();
        }
    }

    private void onWithdrawClicked() {
        double amount = getWithdrawalAmount();
        String payout = getPayoutChoice();

        if (amount > 0 && !TextUtils.isEmpty(payout)) {
            performTransaction(amount, payout);
        } else {
            showMessage("Invalid amount or choose payout account");
        }
    }

    private String getPayoutChoice() {
        String payout = PayoutChoiceType.PAYTM;

        if (mBankRadioButton.isChecked()) {
            payout = PayoutChoiceType.BANK;
        }

        return payout;
    }

    private void performTransaction(double amount, String type) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            showProgressbar();
            WithdrawFromWalletApiModelImpl.newInstance(getWithdrawApiListener(amount, type)).callWithdrawMoneyApi(amount, type);
        } else {
            showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private WithdrawFromWalletApiModelImpl.WithdrawMoneyFromWalletApiListener getWithdrawApiListener(final double amount, final String payoutType) {
        return new WithdrawFromWalletApiModelImpl.WithdrawMoneyFromWalletApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onNoApiResponse() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(WithdrawFromWalletResponse response) {
                dismissProgressbar();
                handleWithdrawSuccessResponse(response, amount, payoutType);
            }
        };
    }

    private void handleWithdrawSuccessResponse(WithdrawFromWalletResponse response, double amount, String payoutType) {
        if (response != null) {
            switch (response.getCode()) {
                case Constants.WithdrawFromWalletResponseCode.SUCCESS:
                    NostragamusAnalytics.getInstance().trackWalletTransaction(false, amount);
                    showSuccessDialog(response.getCode(), amount, payoutType);
                    break;

                case Constants.WithdrawFromWalletResponseCode.ERROR_INSUFICIENT_BALANCE:
                case Constants.WithdrawFromWalletResponseCode.ERROR_MIN_BALANCE_REQUIRED:
                case Constants.WithdrawFromWalletResponseCode.ERROR_UNKNOWN:
                    showFailureDialog(response.getCode(), amount, payoutType);
                    break;
            }
        }
    }

    private void showFailureDialog(int code,  double amount, String payoutType) {
        if (mFragmentListener != null) {
            mFragmentListener.onWithdrawFailure(getArgs(code, amount, payoutType));
        }
    }

    @NonNull
    private Bundle getArgs(int code, double amount, String payoutType) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.WITHDRAW_STATUS_CODE, code);
        bundle.putDouble(Constants.BundleKeys.WITHDRAW_AMOUNT, amount);
        bundle.putString(Constants.BundleKeys.WITHDRAW_PAYOUT_TYPE, payoutType);
        return bundle;
    }

    private void showSuccessDialog(int code, double amount, String payoutType) {
        if (mFragmentListener != null) {
            mFragmentListener.onWithdrawSuccessful(getArgs(code, amount, payoutType));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.payout_choice_paytm_radio_button:
                if (isChecked) {
                    mBankRadioButton.setChecked(false);
                }
                break;

            case R.id.payout_choice_bank_radio_button:
                if (isChecked) {
                    mPaytmRadioButton.setChecked(false);
                }
                break;
        }
    }
}
