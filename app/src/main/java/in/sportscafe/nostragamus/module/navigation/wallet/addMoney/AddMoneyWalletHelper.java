package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionFailureDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionSuccessDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.PaytmTransactionResponse;

/**
 * Created by sandip on 16/06/17.
 */

public class AddMoneyWalletHelper {

    private static final String TAG = AddMoneyWalletHelper.class.getSimpleName();

    public static double validateAddMoneyAmountValid(String amountStr) {
        double amount = 0;

        if (!TextUtils.isEmpty(amountStr)) {
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        return amount;
    }

    /**
     * Initiate transaction
     *
     * @param amount
     */
    public static void initTransaction(@NonNull BaseFragment fragment, double amount) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            fragment.showProgressbar();
            AddMoneyToWalletApiModelImpl.newInstance(getAddMoneyApiListener(fragment, amount)).callAddMoneyApi(amount);
        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    /**
     * Action handler based on AddMoneyApi response
     *
     * @return
     */
    private static AddMoneyToWalletApiModelImpl.AddMoneyToWalletApiListener getAddMoneyApiListener(final BaseFragment fragment, final double amount) {
        return new AddMoneyToWalletApiModelImpl.AddMoneyToWalletApiListener() {
            @Override
            public void noInternet() {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    fragment.dismissProgressbar();
                    fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
                }
            }

            @Override
            public void onNoApiResponse() {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    fragment.dismissProgressbar();
                    fragment.showMessage(Constants.Alerts.API_FAIL);
                }
            }

            @Override
            public void onSuccessResponse(GenerateOrderResponse response) {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    fragment.dismissProgressbar();

                    handleAddMoneyApiResponse(fragment, response, amount);
                }
            }
        };
    }

    /**
     * A specific case when AddMoneyToWallet successful
     *
     * @param response
     */
    private static void handleAddMoneyApiResponse(BaseFragment fragment, GenerateOrderResponse response, double amount) {
        if (response != null && !TextUtils.isEmpty(response.getCheckSumHash())) {

            performPaytmTransaction(fragment, response, amount);
        } else {
            Log.d(TAG, "Api response OR checkSumHash in (AddMoneyToWallet) Api is null!");
            fragment.showMessage(Constants.Alerts.API_FAIL);
        }
    }

    /**
     * @param generateOrderResponse
     */
    private static void performPaytmTransaction(BaseFragment fragment, GenerateOrderResponse generateOrderResponse, double amount) {
        PaytmApiModelImpl paytmApiModel = PaytmApiModelImpl.newInstance(getPaytmApiListener(fragment,amount), fragment.getContext());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            paytmApiModel.initPaytmTransaction(generateOrderResponse);

        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private static PaytmApiModelImpl.OnPaytmApiModelListener getPaytmApiListener(final BaseFragment fragment, final double amount) {
        return new PaytmApiModelImpl.OnPaytmApiModelListener() {
            @Override
            public void onTransactionUiError() {
                Log.d(TAG, Constants.Alerts.PAYTM_FAILURE);
                showPaytmTransactionFailureDialog(fragment, amount);
            }

            @Override
            public void onTransactionNoNetwork() {
                Log.d(TAG, Constants.Alerts.NO_NETWORK_CONNECTION);
                showPaytmTransactionFailureDialog(fragment, amount);
            }

            @Override
            public void onTransactionClientAuthenticationFailed() {
                Log.d(TAG, Constants.Alerts.PAYTM_AUTHENTICATION_FAILED);
                showPaytmTransactionFailureDialog(fragment, amount);
            }

            @Override
            public void onTransactionPageLoadingError() {
                Log.d(TAG, Constants.Alerts.PAYTM_FAILURE);
                showPaytmTransactionFailureDialog(fragment, amount);
            }

            @Override
            public void onTransactionCancelledByBackPressed() {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog(fragment, amount);
            }

            @Override
            public void onTransactionCancelled() {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog(fragment, amount);
            }

            @Override
            public void onTransactionSuccessResponse(@Nullable PaytmTransactionResponse successResponse) {
                Log.d(TAG, "Paytm Transaction Response - Success");

                NostragamusAnalytics.getInstance().trackWalletTransaction(true, amount);

                showPaytmSuccessDialog(fragment, amount);
            }

            @Override
            public void onTransactionFailureResponse(@Nullable PaytmTransactionResponse response) {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                showPaytmTransactionFailureDialog(fragment, amount);
            }
        };
    }

    private static void showPaytmSuccessDialog(final BaseFragment fragment, final double amount) {
        // As fragment resume may take some time, launch fragment after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PaytmTransactionSuccessDialogFragment successDialogFragment =
                        PaytmTransactionSuccessDialogFragment.newInstance(1200, amount, getPaytmSuccessActionListener(fragment));

                if (fragment != null) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
                    if (fragmentManager != null) {
                        successDialogFragment.show(fragmentManager, "SUCCESS_DIALOG");
                    }
                }
            }
        }, 200);
    }

    private static void showPaytmTransactionFailureDialog(final BaseFragment fragment, final double amount) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PaytmTransactionFailureDialogFragment failureDialogFragment =
                        PaytmTransactionFailureDialogFragment.newInstance(1199, getPaytmFailureActionListener(fragment, amount));

                if (fragment != null) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
                    if (fragmentManager != null) {
                        failureDialogFragment.show(fragmentManager, "FAILURE_DIALOG");
                    }
                }
            }
        }, 200);
    }

    /**
     * Paytm transaction failed, then failure dialog button click handler
     *
     * @return
     */
    private static PaytmTransactionFailureDialogFragment.IPaytmFailureActionListener
    getPaytmFailureActionListener(final BaseFragment fragment, final double amount) {
        return new PaytmTransactionFailureDialogFragment.IPaytmFailureActionListener() {

            @Override
            public void onBackToAddMoney() {
                Log.d(TAG, "On Paytm transaction failed ");
            }

            @Override
            public void onRetryPayment() {
                Log.d(TAG, "On Paytm transaction failed - Retrying... ");
                initTransaction(fragment, amount);
            }
        };
    }

    /**
     * Paytm transaction success, then success dialog button click handler
     *
     * @return
     */
    private static PaytmTransactionSuccessDialogFragment.IPaytmSuccessActionListener
    getPaytmSuccessActionListener(final BaseFragment fragment) {
        return new PaytmTransactionSuccessDialogFragment.IPaytmSuccessActionListener() {
            @Override
            public void onBackToHomeClicked() {
                if (fragment != null) {
                    if (fragment instanceof  AddWalletMoneyFragment) {
                        AddWalletMoneyFragmentListener fragmentListener = ((AddWalletMoneyFragment) fragment).getFragmentListener();
                        fragmentListener.onSuccess();
                    }
                    if (fragment instanceof AddMoneyOnLowBalanceFragment) {
                        /*AddMoneyOnLowBalanceFragmentListener fragmentListener = ((AddMoneyOnLowBalanceFragment) fragment).getFragmentListener();
                        fragmentListener.onTimerSuccess();*/

                        ((AddMoneyOnLowBalanceFragment) fragment).onAddMoneySuccess();
                    }
                }
            }
        };
    }


}
