package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionFailureDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.PaytmTransactionResponse;

public class AddWalletMoneyFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AddWalletMoneyFragment.class.getSimpleName();

    private AddWalletMoneyFragmentListener mFragmentListener;
    private EditText mAmountEditText;

    public AddWalletMoneyFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddWalletMoneyFragmentListener) {
            mFragmentListener = (AddWalletMoneyFragmentListener) context;
        } else {
            throw  new RuntimeException("Activity must implement " +
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
            if (getView() != null) {
                Snackbar.make(getView(), "Please enter amount", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Initiate transaction
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
     *
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
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionNoNetwork() {
                Log.d(TAG, Constants.Alerts.NO_NETWORK_CONNECTION);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionClientAuthenticationFailed() {
                Log.d(TAG, Constants.Alerts.PAYTM_AUTHENTICATION_FAILED);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionPageLoadingError() {
                Log.d(TAG, Constants.Alerts.PAYTM_FAILURE);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionCancelledByBackPressed() {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionCancelled() {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionSuccessResponse(@Nullable PaytmTransactionResponse successResponse) {
                Log.d(TAG, "Paytm Transaction Response - Success");

                NostragamusAnalytics.getInstance().trackWalletTransaction(true, amount);

                showPaytmSuccessDialog();
            }

            @Override
            public void onTransactionFailureResponse(@Nullable PaytmTransactionResponse response) {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                showPaytmTransactionFailureDialog();
            }
        };
    }

    private void showPaytmSuccessDialog() {
        // TODO: paytm success receipt
        showMessage("Paytm Successful");
    }

    private void showPaytmTransactionFailureDialog() {
            /*PaytmTransactionFailureDialogFragment failureDialogFragment =
                    PaytmTransactionFailureDialogFragment.newInstance(1199, null, getPaytmFailureActionListener());
            failureDialogFragment.show(getChildFragmentManager(), "FAILURE_DIALOG");*/
            showMessage("Transaction Failed");
    }

    /**
     * Paytm transaction failed, then failure dialog button click handler
     * @return
     */
    private PaytmTransactionFailureDialogFragment.IPaytmFailureActionListener getPaytmFailureActionListener() {
        // TODO : need some change in handling, as design get changed
        return new PaytmTransactionFailureDialogFragment.IPaytmFailureActionListener() {
            @Override
            public void onRejoinClicked() {
                showMessage("Rejoin Clicked");
            }

            @Override
            public void onBackToHomeClicked() {
                showMessage("Back To Home Clicked");
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
