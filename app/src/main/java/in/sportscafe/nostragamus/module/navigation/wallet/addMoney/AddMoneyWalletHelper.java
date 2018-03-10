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
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree.AddMoneyByCashFreeHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeTransactionResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode.SelectPaymentModeFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode.SelectPaymentModeFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.TransactionFailureDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.TransactionPendingDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.TransactionSuccessDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.PaytmTransactionResponse;

/**
 * Created by sandip on 16/06/17.
 */

public class AddMoneyWalletHelper {

    private static final String TAG = AddMoneyWalletHelper.class.getSimpleName();

    private static AddMoneyProcessListener addMoneyProcessListener;

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
    public static void initTransaction(@NonNull BaseFragment fragment, double amount, @NonNull AddMoneyProcessListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection() && listener != null) {
            addMoneyProcessListener = listener;
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
                verifyPaymentApi(fragment, successResponse, amount);
            }

            @Override
            public void onTransactionFailureResponse(@Nullable PaytmTransactionResponse response) {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                showPaytmTransactionFailureDialog(fragment, amount);
            }
        };
    }


    private static void verifyPaymentApi(BaseFragment fragment, PaytmTransactionResponse successResponse, double amount) {
        VerifyPaymentApiImpl verifyPaymentApi = VerifyPaymentApiImpl.newInstance(getVerifyPaymentApiListener(fragment, successResponse, amount), fragment.getContext());

        if (Nostragamus.getInstance().hasNetworkConnection() && successResponse != null
                && !TextUtils.isEmpty(successResponse.getOrderId())) {
            if (addMoneyProcessListener != null) {
                addMoneyProcessListener.showProgressBar();
            }
            verifyPaymentApi.verifyPayment(successResponse.getOrderId());
        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }

    }

    private static VerifyPaymentApiImpl.VerifyPaymentApiListener getVerifyPaymentApiListener(final BaseFragment fragment, PaytmTransactionResponse successResponse, final double amount) {
        return new VerifyPaymentApiImpl.VerifyPaymentApiListener() {

            @Override
            public void onFailure(int dataStatus) {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    if (addMoneyProcessListener!=null){
                        addMoneyProcessListener.hideProgressBar();
                    }
                    Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                    showPaytmTransactionFailureDialog(fragment, amount);
                }
            }

            @Override
            public void onSuccessResponse(String orderStatus) {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    if (addMoneyProcessListener!=null){
                        addMoneyProcessListener.hideProgressBar();
                    }
                    if (orderStatus.equalsIgnoreCase(Constants.VerifyPaymentStatus.SUCCESS)) {
                        Log.d(TAG, "Cashfree Transaction Response - Success from Server");
                        NostragamusAnalytics.getInstance().trackWalletTransaction(true, amount);
                        showPaytmSuccessDialog(fragment, amount);
                    } else {
                        Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                        showPaytmTransactionFailureDialog(fragment, amount);
                    }
                }
            }

            @Override
            public void onPendingResponse() {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    if (addMoneyProcessListener!=null){
                        addMoneyProcessListener.hideProgressBar();
                    }
                    Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                    showTransactionPendingDialog(fragment,amount);
                }
            }

        };
    }

    private static void showPaytmSuccessDialog(final BaseFragment fragment, final double amount) {
        // As fragment resume may take some time, launch fragment after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransactionSuccessDialogFragment successDialogFragment =
                        TransactionSuccessDialogFragment.newInstance(1200, amount, getPaytmSuccessActionListener(fragment));

                if (fragment != null && fragment.getActivity()!=null && !fragment.getActivity().isFinishing()) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
                    if (fragmentManager != null) {
                        successDialogFragment.showDialogAllowingStateLoss(fragmentManager,successDialogFragment,"SUCCESS_DIALOG");
                    }
                }
            }
        }, 200);
    }

    private static void showPaytmTransactionFailureDialog(final BaseFragment fragment, final double amount) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransactionFailureDialogFragment failureDialogFragment =
                        TransactionFailureDialogFragment.newInstance(1199, getPaytmFailureActionListener(fragment, amount));

                if (fragment != null && fragment.getActivity()!=null && !fragment.getActivity().isFinishing()) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
                    if (fragmentManager != null) {
                        try {
                            failureDialogFragment.showDialogAllowingStateLoss(fragmentManager,failureDialogFragment, "FAILURE_DIALOG");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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
    private static TransactionFailureDialogFragment.IFailureActionListener
    getPaytmFailureActionListener(final BaseFragment fragment, final double amount) {
        return new TransactionFailureDialogFragment.IFailureActionListener() {

            @Override
            public void onBackToSelectPaymentMode() {
                Log.d(TAG, "On Paytm transaction failed ");
            }

            @Override
            public void onRetryPayment() {
                Log.d(TAG, "On Paytm transaction failed - Retrying... ");
                if (addMoneyProcessListener != null) {
                    initTransaction(fragment, amount, addMoneyProcessListener);
                }
            }
        };
    }

    /**
     * Paytm transaction success, then success dialog button click handler
     *
     * @return
     */
    private static TransactionSuccessDialogFragment.ISuccessActionListener
    getPaytmSuccessActionListener(final BaseFragment fragment) {
        return new TransactionSuccessDialogFragment.ISuccessActionListener() {
            @Override
            public void onBackToHomeClicked() {
                if (fragment != null) {
                    if (fragment instanceof AddMoneyThroughPaytmFragment) {
                        AddMoneyThroughPaytmFragmentListener fragmentListener = ((AddMoneyThroughPaytmFragment) fragment).getFragmentListener();
                        if (fragmentListener != null) {
                            fragmentListener.onPaytmMoneyAddSuccess();
                        }
                    }
                    if (fragment instanceof AddMoneyThroughPaymentCouponFragment) {
                        AddMoneyThroughPaymentCouponFragmentListener fragmentListener = ((AddMoneyThroughPaymentCouponFragment) fragment).getFragmentListener();
                        if (fragmentListener != null) {
                            fragmentListener.onPaytmMoneyAddSuccess();
                        }
                    }
                    if (fragment instanceof SelectPaymentModeFragment) {
                        SelectPaymentModeFragmentListener fragmentListener = ((SelectPaymentModeFragment) fragment).getFragmentListener();
                        if (fragmentListener != null) {
                            fragmentListener.onMoneyAddedToWalletSuccess();
                        }
                    }


                    /*if (fragment instanceof  AddWalletMoneyFragment) {
                        AddWalletMoneyFragmentListener fragmentListener = ((AddWalletMoneyFragment) fragment).getFragmentListener();
                        fragmentListener.onSuccess();
                    }
                    if (fragment instanceof AddMoneyOnLowBalanceFragment) {
                        *//*AddMoneyOnLowBalanceFragmentListener fragmentListener = ((AddMoneyOnLowBalanceFragment) fragment).getFragmentListener();
                        fragmentListener.onTimerSuccess();*//*

                        ((AddMoneyOnLowBalanceFragment) fragment).onAddMoneySuccess();
                    }*/
                }
            }
        };
    }

    private static void showTransactionPendingDialog(final BaseFragment fragment, final double amount) {
        // As fragment resume may take some time, launch fragment after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransactionPendingDialogFragment pendingDialogFragment =
                        TransactionPendingDialogFragment.newInstance(1201, amount, getTransactionPendingActionListener(fragment));

                if (fragment != null && fragment.getActivity() != null && !fragment.getActivity().isFinishing()) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
                    if (fragmentManager != null) {
                        pendingDialogFragment.showDialogAllowingStateLoss(fragmentManager, pendingDialogFragment, "PENDING_DIALOG");
                    }
                }
            }
        }, 200);
    }

    /**
     * Paytm transaction pending , then Pending dialog button click handler
     *
     * @return
     */
    private static TransactionPendingDialogFragment.IPendingActionListener
    getTransactionPendingActionListener(final BaseFragment fragment) {
        return new TransactionPendingDialogFragment.IPendingActionListener() {

            @Override
            public void onBackToWallet() {
                Log.d(TAG, "On transaction Pending ");
            }

        };
    }


}
