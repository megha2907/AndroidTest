package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionFailureDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionSuccessDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.PaytmTransactionResponse;

public class AddWalletMoneyFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AddWalletMoneyFragment.class.getSimpleName();

    private AddWalletMoneyFragmentListener mFragmentListener;
    private EditText mAmountEditText;

    public AddWalletMoneyFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddWalletMoneyFragmentListener) {
            mFragmentListener = (AddWalletMoneyFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " +
                    AddWalletMoneyFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_wallet_money, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.back_button).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_50_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_100_textView).setOnClickListener(this);
        rootView.findViewById(R.id.add_money_250_textView).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_add_amount_button).setOnClickListener(this);

        mAmountEditText = (EditText) rootView.findViewById(R.id.wallet_add_amount_editText);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        showWalletBalance();
    }

    private void showWalletBalance() {
        if (getView() != null) {
            double amount = WalletHelper.getBalanceAmount();
            if (amount > 0) {
                TextView balanceTextView = (TextView) getView().findViewById(R.id.wallet_add_money_amount_textView);
                balanceTextView.setText(WalletHelper.getFormattedStringOfAmount(amount));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;

            case R.id.add_money_50_textView:
                onAddMoney50Clicked();
                break;

            case R.id.add_money_100_textView:
                onAddMoney100Clicked();
                break;

            case R.id.add_money_250_textView:
                onAddMoney250Clicked();
                break;

            case R.id.wallet_add_amount_button:
                onAddAmountClicked();
                break;
        }
    }

    private void onAddAmountClicked() {
        double amount = validateAmount();
        if (amount > 0) {
            initTransaction(amount);
        } else {
            /*if (getView() != null) {
                Snackbar.make(getView(), "Please enter amount", Snackbar.LENGTH_SHORT).show();
            }*/
            if (mAmountEditText != null) {
                mAmountEditText.setError("Please enter amount");
            }
        }
    }

    /**
     * Initiate transaction
     *
     * @param amount
     */
    private void initTransaction(double amount) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            showProgressbar();
            AddMoneyToWalletApiModelImpl.newInstance(getAddMoneyApiListener(amount)).callAddMoneyApi(amount);
        } else {
            showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    /**
     * Action handler based on AddMoneyApi response
     *
     * @return
     */
    private AddMoneyToWalletApiModelImpl.AddMoneyToWalletApiListener getAddMoneyApiListener(final double amount) {
        return new AddMoneyToWalletApiModelImpl.AddMoneyToWalletApiListener() {
            @Override
            public void noInternet() {
                if (getActivity() != null && getView() != null) {
                    dismissProgressbar();
                    showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
                }
            }

            @Override
            public void onNoApiResponse() {
                if (getActivity() != null && getView() != null) {
                    dismissProgressbar();
                    showMessage(Constants.Alerts.API_FAIL);
                }
            }

            @Override
            public void onSuccessResponse(GenerateOrderResponse response) {
                if (getActivity() != null && getView() != null) {
                    dismissProgressbar();

                    handleAddMoneyApiResponse(response, amount);
                }
            }
        };
    }

    /**
     * A specific case when AddMoneyToWallet successful
     *
     * @param response
     */
    private void handleAddMoneyApiResponse(GenerateOrderResponse response, double amount) {
        if (response != null && !TextUtils.isEmpty(response.getCheckSumHash())) {

            performPaytmTransaction(response, amount);
        } else {
            Log.d(TAG, "Api response OR checkSumHash in (AddMoneyToWallet) Api is null!");
            showMessage(Constants.Alerts.API_FAIL);
        }
    }

    /**
     * @param generateOrderResponse
     */
    private void performPaytmTransaction(GenerateOrderResponse generateOrderResponse, double amount) {
        PaytmApiModelImpl paytmApiModel = PaytmApiModelImpl.newInstance(getPaytmApiListener(amount), getContext());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            paytmApiModel.initPaytmTransaction(generateOrderResponse);

        } else {
            showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private PaytmApiModelImpl.OnPaytmApiModelListener getPaytmApiListener(final double amount) {
        return new PaytmApiModelImpl.OnPaytmApiModelListener() {
            @Override
            public void onTransactionUiError() {
                Log.d(TAG, Constants.Alerts.PAYTM_FAILURE);
                showPaytmTransactionFailureDialog(amount);
            }

            @Override
            public void onTransactionNoNetwork() {
                Log.d(TAG, Constants.Alerts.NO_NETWORK_CONNECTION);
                showPaytmTransactionFailureDialog(amount);
            }

            @Override
            public void onTransactionClientAuthenticationFailed() {
                Log.d(TAG, Constants.Alerts.PAYTM_AUTHENTICATION_FAILED);
                showPaytmTransactionFailureDialog(amount);
            }

            @Override
            public void onTransactionPageLoadingError() {
                Log.d(TAG, Constants.Alerts.PAYTM_FAILURE);
                showPaytmTransactionFailureDialog(amount);
            }

            @Override
            public void onTransactionCancelledByBackPressed() {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog(amount);
            }

            @Override
            public void onTransactionCancelled() {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog(amount);
            }

            @Override
            public void onTransactionSuccessResponse(@Nullable PaytmTransactionResponse successResponse) {
                Log.d(TAG, "Paytm Transaction Response - Success");

                NostragamusAnalytics.getInstance().trackWalletTransaction(true, amount);

                showPaytmSuccessDialog(amount);
            }

            @Override
            public void onTransactionFailureResponse(@Nullable PaytmTransactionResponse response) {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                showPaytmTransactionFailureDialog(amount);
            }
        };
    }

    private void showPaytmSuccessDialog(final double amount) {
        // As fragment resume may take some time, launch fragment after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PaytmTransactionSuccessDialogFragment successDialogFragment =
                        PaytmTransactionSuccessDialogFragment.newInstance(1200, amount, getPaytmSuccessActionListener());
                successDialogFragment.show(getChildFragmentManager(), "SUCCESS_DIALOG");
            }
        }, 200);
    }

    private void showPaytmTransactionFailureDialog(final double amount) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PaytmTransactionFailureDialogFragment failureDialogFragment =
                        PaytmTransactionFailureDialogFragment.newInstance(1199, getPaytmFailureActionListener(amount));
                failureDialogFragment.show(getChildFragmentManager(), "FAILURE_DIALOG");
            }
        }, 200);
    }

    /**
     * Paytm transaction failed, then failure dialog button click handler
     *
     * @return
     */
    private PaytmTransactionFailureDialogFragment.IPaytmFailureActionListener getPaytmFailureActionListener(final double amount) {
        return new PaytmTransactionFailureDialogFragment.IPaytmFailureActionListener() {

            @Override
            public void onBackToAddMoney() {
                Log.d(TAG, "On Paytm transaction failed ");
            }

            @Override
            public void onRetryPayment() {
                Log.d(TAG, "On Paytm transaction failed - Retrying... ");
                initTransaction(amount);
            }
        };
    }

    /**
     * Paytm transaction success, then success dialog button click handler
     *
     * @return
     */
    private PaytmTransactionSuccessDialogFragment.IPaytmSuccessActionListener getPaytmSuccessActionListener() {
        return new PaytmTransactionSuccessDialogFragment.IPaytmSuccessActionListener() {
            @Override
            public void onBackToHomeClicked() {
                if (mFragmentListener != null) {
                    mFragmentListener.onSuccess();
                }
            }
        };
    }

    private double validateAmount() {
        double amount = 0;

        String str = mAmountEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(str)) {
            try {
                amount = Double.parseDouble(str);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        return amount;
    }

    private void onAddMoney250Clicked() {
        mAmountEditText.setText("250");
        setEditTextSelection();
    }

    private void onAddMoney100Clicked() {
        mAmountEditText.setText("100");
        setEditTextSelection();
    }

    private void onAddMoney50Clicked() {
        mAmountEditText.setText("50");
        setEditTextSelection();
    }

    private void setEditTextSelection() {
        int length = mAmountEditText.getText().length();
        mAmountEditText.setSelection(length, length);
    }
}
