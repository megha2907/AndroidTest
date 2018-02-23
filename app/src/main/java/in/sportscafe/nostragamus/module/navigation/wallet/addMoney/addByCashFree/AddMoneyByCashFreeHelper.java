package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeGenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeTransactionResponse;

/**
 * Created by deepanshi on 2/23/18.
 */

public class AddMoneyByCashFreeHelper {

    private static final String TAG = AddMoneyByCashFreeHelper.class.getSimpleName();

    /**
     * Generate Order and Get OrderDetails From server
     *
     * @param amount
     */
    public static void getOrderDetails(@NonNull BaseFragment fragment, double amount) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            fragment.showProgressbar();
            GenerateCashFreeOrderApiModelImpl.newInstance(getGenerateCashFreeOrderApiListener(fragment, amount)).callGenerateOrderApi(amount);
        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    /**
     * Action handler based on Generate Order response
     *
     * @return
     */
    private static GenerateCashFreeOrderApiModelImpl.GenerateCashFreeOrderApiListener getGenerateCashFreeOrderApiListener(final BaseFragment fragment, final double amount) {
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

                    handleCashFreeGenerateOrderResponse(fragment, response, amount);
                }
            }

        };
    }

    /**
     * A specific case when AddMoneyToWallet successful
     *
     * @param response
     */
    private static void handleCashFreeGenerateOrderResponse(BaseFragment fragment, CashFreeGenerateOrderResponse response, double amount) {
        if (response != null) {
            performCashFreeTransaction(fragment, response, amount);
        } else {
            Log.d(TAG, "Api response OR checkSumHash in (AddMoneyToWallet) Api is null!");
            fragment.showMessage(Constants.Alerts.API_FAIL);
        }
    }

    /**
     * @param cashFreeGenerateOrderResponse
     */
    private static void performCashFreeTransaction(BaseFragment fragment, CashFreeGenerateOrderResponse cashFreeGenerateOrderResponse, double amount) {
        CashFreeApiModelImpl cashFreeApiModel = CashFreeApiModelImpl.newInstance(getCashFreeApiListener(fragment, amount), fragment.getContext());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            cashFreeApiModel.initCashFreeTransaction(cashFreeGenerateOrderResponse);
        } else {
            fragment.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
        }
    }

    private static CashFreeApiModelImpl.OnCashFreeApiModelListener getCashFreeApiListener(final BaseFragment fragment, final double amount) {
        return new CashFreeApiModelImpl.OnCashFreeApiModelListener() {

            @Override
            public void onTransactionUiError() {

            }

            @Override
            public void onTransactionNoNetwork() {

            }

            @Override
            public void onTransactionClientAuthenticationFailed() {

            }

            @Override
            public void onTransactionPageLoadingError() {

            }

            @Override
            public void onTransactionCancelledByBackPressed() {

            }

            @Override
            public void onTransactionCancelled() {

            }

            @Override
            public void onTransactionSuccessResponse(CashFreeTransactionResponse response) {

            }

            @Override
            public void onTransactionFailureResponse(CashFreeTransactionResponse response) {

            }
        };
    }

}
