package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.CustomProgressbar;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.contest.dataProvider.JoinContestApiImpl;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.dto.VerifyJoinContestResponse;
import in.sportscafe.nostragamus.module.contest.helper.JoinContestHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.VerifyPaymentApiImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon.AddMoneyThroughPaymentCouponFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyThroughPaytmFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeGenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeTransactionResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode.SelectPaymentModeFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode.SelectPaymentModeFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.TransactionFailureDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.TransactionSuccessDialogFragment;

/**
 * Created by deepanshi on 2/23/18.
 */

public class AddMoneyByCashFreeHelper {

    private static final String TAG = AddMoneyByCashFreeHelper.class.getSimpleName();

    private static AddMoneyByCashFreeProcessListener addMoneyByCashFreeProcessListener;

    public interface AddMoneyByCashFreeProcessListener {
        void noInternet();
        void hideProgressBar();
        void showProgressBar();
    }

    /**
     * Generate Order and Get OrderDetails From server
     *
     * @param amount
     */
    public static void getOrderDetails(@NonNull BaseFragment fragment, double amount, String paymentMode, @NonNull AddMoneyByCashFreeHelper.AddMoneyByCashFreeProcessListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection() && listener != null) {
            addMoneyByCashFreeProcessListener = listener;
            fragment.showProgressbar();
            GenerateCashFreeOrderApiModelImpl.newInstance(getGenerateCashFreeOrderApiListener(fragment, amount, paymentMode)).callGenerateOrderApi(amount);
        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    /**
     * Action handler based on Generate Order response
     *
     * @return
     */
    private static GenerateCashFreeOrderApiModelImpl.GenerateCashFreeOrderApiListener getGenerateCashFreeOrderApiListener(final BaseFragment fragment, final double amount, final String paymentMode) {
        return new GenerateCashFreeOrderApiModelImpl.GenerateCashFreeOrderApiListener() {
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
            public void onSuccessResponse(CashFreeGenerateOrderResponse response) {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    fragment.dismissProgressbar();

                    handleCashFreeGenerateOrderResponse(fragment, response, amount, paymentMode);
                }
            }

        };
    }

    /**
     * A specific case when AddMoneyToWallet successful
     *
     * @param response
     * @param paymentMode
     */
    private static void handleCashFreeGenerateOrderResponse(BaseFragment fragment, CashFreeGenerateOrderResponse response, double amount, String paymentMode) {
        if (response != null) {
            performCashFreeTransaction(fragment, response, amount, paymentMode);
        } else {
            Log.d(TAG, "Api response OR checkSumHash in (AddMoneyToWallet) Api is null!");
            fragment.showMessage(Constants.Alerts.API_FAIL);
        }
    }

    /**
     * @param cashFreeGenerateOrderResponse
     * @param paymentMode
     */
    private static void performCashFreeTransaction(BaseFragment fragment, CashFreeGenerateOrderResponse cashFreeGenerateOrderResponse, double amount, String paymentMode) {
        CashFreeApiModelImpl cashFreeApiModel = CashFreeApiModelImpl.newInstance(getCashFreeApiListener(fragment, amount, paymentMode), fragment.getContext());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            cashFreeApiModel.initCashFreeTransaction(cashFreeGenerateOrderResponse, paymentMode);
        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private static CashFreeApiModelImpl.OnCashFreeApiModelListener getCashFreeApiListener(final BaseFragment fragment, final double amount, final String paymentMode) {
        return new CashFreeApiModelImpl.OnCashFreeApiModelListener() {

            @Override
            public void onTransactionUiError() {
                Log.d(TAG, Constants.Alerts.PAYTM_FAILURE);
                showCashfreeTransactionFailureDialog(fragment, amount, paymentMode);
            }

            @Override
            public void onTransactionPageLoadingError() {
                Log.d(TAG, Constants.Alerts.PAYTM_FAILURE);
                showCashfreeTransactionFailureDialog(fragment, amount, paymentMode);
            }

            @Override
            public void onTransactionCancelledByBackPressed() {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_CANCELLED);
                showCashfreeTransactionFailureDialog(fragment, amount, paymentMode);
            }

            @Override
            public void onTransactionSuccessResponse(CashFreeTransactionResponse response) {
                Log.d(TAG, "Cashfree Transaction Response - Success from SDK, Now Verify Payment");
                verifyPaymentApi(fragment, response, amount, paymentMode);
            }

            @Override
            public void onTransactionFailureResponse(CashFreeTransactionResponse response) {
                Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                showCashfreeTransactionFailureDialog(fragment, amount, paymentMode);
            }
        };
    }

    private static void verifyPaymentApi(BaseFragment fragment, CashFreeTransactionResponse cashFreeTransactionResponse, double amount, String paymentMode) {
        VerifyPaymentApiImpl verifyPaymentApi = VerifyPaymentApiImpl.newInstance(getVerifyPaymentApiListener(fragment, cashFreeTransactionResponse, amount, paymentMode), fragment.getContext());

        if (Nostragamus.getInstance().hasNetworkConnection() && cashFreeTransactionResponse != null
                && !TextUtils.isEmpty(cashFreeTransactionResponse.getOrderId())) {
            if (addMoneyByCashFreeProcessListener != null) {
                addMoneyByCashFreeProcessListener.showProgressBar();
            }
            verifyPaymentApi.verifyPayment(cashFreeTransactionResponse.getOrderId());
        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }

    }

    private static VerifyPaymentApiImpl.VerifyPaymentApiListener getVerifyPaymentApiListener(final BaseFragment fragment, CashFreeTransactionResponse cashFreeTransactionResponse, final double amount, final String paymentMode) {
        return new VerifyPaymentApiImpl.VerifyPaymentApiListener() {

            @Override
            public void onFailure(int dataStatus) {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    if (addMoneyByCashFreeProcessListener!=null){
                        addMoneyByCashFreeProcessListener.hideProgressBar();
                    }
                    Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                    showCashfreeTransactionFailureDialog(fragment, amount, paymentMode);
                }
            }

            @Override
            public void onSuccessResponse(String orderStatus) {
                if (fragment.getActivity() != null && fragment.getView() != null) {
                    if (addMoneyByCashFreeProcessListener!=null){
                        addMoneyByCashFreeProcessListener.hideProgressBar();
                    }
                    if (orderStatus.equalsIgnoreCase(Constants.VerifyPaymentStatus.SUCCESS)) {
                        Log.d(TAG, "Cashfree Transaction Response - Success from Server");
                        NostragamusAnalytics.getInstance().trackWalletTransaction(true, amount);
                        showCashfreeSuccessDialog(fragment, amount);
                    } else {
                        Log.d(TAG, Constants.Alerts.PAYTM_TRANSACTION_FAILED);
                        showCashfreeTransactionFailureDialog(fragment, amount, paymentMode);
                    }
                }
            }

        };
    }

    private static void showCashfreeSuccessDialog(final BaseFragment fragment, final double amount) {
        // As fragment resume may take some time, launch fragment after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransactionSuccessDialogFragment successDialogFragment =
                        TransactionSuccessDialogFragment.newInstance(1200, amount, getTransactionSuccessActionListener(fragment));

                if (fragment != null && fragment.getActivity() != null && !fragment.getActivity().isFinishing()) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
                    if (fragmentManager != null) {
                        successDialogFragment.showDialogAllowingStateLoss(fragmentManager, successDialogFragment, "SUCCESS_DIALOG");
                    }
                }
            }
        }, 200);
    }

    private static void showCashfreeTransactionFailureDialog(final BaseFragment fragment, final double amount, final String paymentMode) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransactionFailureDialogFragment failureDialogFragment =
                        TransactionFailureDialogFragment.newInstance(1199, getTransactionFailureActionListener(fragment, amount, paymentMode));

                if (fragment != null && fragment.getActivity() != null && !fragment.getActivity().isFinishing()) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
                    if (fragmentManager != null) {
                        try {
                            failureDialogFragment.showDialogAllowingStateLoss(fragmentManager, failureDialogFragment, "FAILURE_DIALOG");
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
    getTransactionFailureActionListener(final BaseFragment fragment, final double amount, final String paymentMode) {
        return new TransactionFailureDialogFragment.IFailureActionListener() {

            @Override
            public void onBackToSelectPaymentMode() {
                Log.d(TAG, "On transaction failed ");
            }

            @Override
            public void onRetryPayment() {
                Log.d(TAG, "On Cashfree transaction failed - Retrying... ");
                if (addMoneyByCashFreeProcessListener != null) {
                    getOrderDetails(fragment, amount, paymentMode, addMoneyByCashFreeProcessListener);
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
    getTransactionSuccessActionListener(final BaseFragment fragment) {
        return new TransactionSuccessDialogFragment.ISuccessActionListener() {
            @Override
            public void onBackToHomeClicked() {
                if (fragment != null) {
                    if (fragment instanceof SelectPaymentModeFragment) {
                        SelectPaymentModeFragmentListener fragmentListener = ((SelectPaymentModeFragment) fragment).getFragmentListener();
                        if (fragmentListener != null) {
                            fragmentListener.onMoneyAddedToWalletSuccess();
                        }
                    }

                }
            }
        };
    }


}
