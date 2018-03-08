package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeGenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeTransactionResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.PaytmTransactionResponse;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_NOTIFY_URL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_MODES;

/**
 * Created by deepanshi on 2/14/18.
 */

public class CashFreeApiModelImpl implements CFClientInterface {

    private static final String TAG = CashFreeApiModelImpl.class.getSimpleName();

    private CashFreeApiModelImpl.OnCashFreeApiModelListener mListener;
    private Context mContext;

    /* stage identifies whether you want trigger test or production service */
    private String STAGE = "PROD";

    private CashFreeApiModelImpl(CashFreeApiModelImpl.OnCashFreeApiModelListener listener, @NonNull Context context) {
        this.mListener = listener;
        this.mContext = context;
    }

    public static CashFreeApiModelImpl newInstance(CashFreeApiModelImpl.OnCashFreeApiModelListener listener, Context context) {
        return new CashFreeApiModelImpl(listener, context);
    }

    public void initCashFreeTransaction(CashFreeGenerateOrderResponse cashFreeGenerateOrderResponse, String paymentMode) {

        if (cashFreeGenerateOrderResponse != null && !TextUtils.isEmpty(cashFreeGenerateOrderResponse.getChecksumUrl())
                && mContext != null && !TextUtils.isEmpty(cashFreeGenerateOrderResponse.getCashFreeAppId())) {

            String checksumUrl = cashFreeGenerateOrderResponse.getChecksumUrl();
            String appId = mContext.getString(R.string.cashfree_app_id);

            Map<String, String> params = new HashMap<>();
            params.put(PARAM_APP_ID, cashFreeGenerateOrderResponse.getCashFreeAppId());
            params.put(PARAM_ORDER_ID, cashFreeGenerateOrderResponse.getOrderId());
            params.put(PARAM_ORDER_AMOUNT, String.valueOf(cashFreeGenerateOrderResponse.getOrderAmount()));
            params.put(PARAM_ORDER_NOTE, cashFreeGenerateOrderResponse.getOrderNote());
            params.put(PARAM_CUSTOMER_NAME, cashFreeGenerateOrderResponse.getCustomerName());
            params.put(PARAM_CUSTOMER_PHONE, cashFreeGenerateOrderResponse.getCustomerPhone());
            params.put(PARAM_CUSTOMER_EMAIL, cashFreeGenerateOrderResponse.getCustomerEmail());
            params.put(PARAM_NOTIFY_URL, cashFreeGenerateOrderResponse.getCallbackUrl());
            params.put(PARAM_PAYMENT_MODES, paymentMode);

            CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
            cfPaymentService.doPayment(mContext, params, checksumUrl, this, STAGE);

        } else {
            com.jeeva.android.Log.d(TAG, "Generate Order Null or CheckSum Url Null");
            if (mListener != null) {
                mListener.onTransactionPageLoadingError();
            }
        }
    }

    @Override
    public void onSuccess(Map<String, String> map) {

        CashFreeTransactionResponse cashFreeTransactionResponse = getCashFreeTransactionResponse(map);
        if (cashFreeTransactionResponse != null && cashFreeTransactionResponse.getTxStatus().equals(Constants.CashFreeTransactionResponseStatusValues.TRANSACTION_SUCCESS)) {
            com.jeeva.android.Log.d(TAG, "CashFree Transaction status SUCCESS ...");
            if (mListener != null) {
                mListener.onTransactionSuccessResponse(cashFreeTransactionResponse);
            }
        } else {
            com.jeeva.android.Log.d(TAG, "CashFree Transaction status NOT / OTHER than SUCCESS ");
        }

    }

    private CashFreeTransactionResponse getCashFreeTransactionResponse(Map<String, String> map) {

        CashFreeTransactionResponse cashFreeTransactionResponse = null;
        if (map != null) {
            cashFreeTransactionResponse = new CashFreeTransactionResponse();
            for (String key : map.keySet()) {
                cashFreeTransactionResponse.setOrderId(map.get(Constants.CashFreeParamsKeys.ORDER_ID));
                cashFreeTransactionResponse.setOrderAmount(map.get(Constants.CashFreeParamsKeys.ORDER_AMOUNT));
                cashFreeTransactionResponse.setReferenceId(map.get(Constants.CashFreeParamsKeys.REFERENCE_ID));
                cashFreeTransactionResponse.setTxStatus(map.get(Constants.CashFreeParamsKeys.TRANSACTION_STATUS));
                cashFreeTransactionResponse.setPaymentMode(map.get(Constants.CashFreeParamsKeys.PAYMENT_MODE));
                cashFreeTransactionResponse.setTxMsg(map.get(Constants.CashFreeParamsKeys.TRANSACTION_MESSAGE));
                cashFreeTransactionResponse.setTxTime(map.get(Constants.CashFreeParamsKeys.TRANSACTION_TIME));
            }

        }
        return cashFreeTransactionResponse;
    }

    @Override
    public void onFailure(Map<String, String> map) {

        CashFreeTransactionResponse cashFreeTransactionResponse = getCashFreeTransactionResponse(map);
        if (cashFreeTransactionResponse != null) {
            if (mListener != null) {
                mListener.onTransactionFailureResponse(cashFreeTransactionResponse);
            }
        } else {
            if (mListener != null) {
                mListener.onTransactionUiError();
            }
        }
    }

    @Override
    public void onNavigateBack() {
        if (mListener != null) {
            mListener.onTransactionCancelledByBackPressed();
        }
    }

    public interface OnCashFreeApiModelListener {
        void onTransactionUiError();

        void onTransactionPageLoadingError();

        void onTransactionCancelledByBackPressed();

        void onTransactionSuccessResponse(CashFreeTransactionResponse response);

        void onTransactionFailureResponse(CashFreeTransactionResponse response);
    }
}
