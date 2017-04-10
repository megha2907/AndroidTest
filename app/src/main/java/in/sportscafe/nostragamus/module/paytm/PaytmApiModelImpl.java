package in.sportscafe.nostragamus.module.paytm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;

/**
 * Created by sandip on 8/4/17.
 */

public class PaytmApiModelImpl {

    private static final String TAG = PaytmApiModelImpl.class.getSimpleName();

    private OnPaytmApiModelListener mListener;
    private Context mContext;

    private PaytmApiModelImpl(OnPaytmApiModelListener listener, @NonNull Context context) {
        this.mListener = listener;
        this.mContext = context;
    }

    public static PaytmApiModelImpl newInstance(OnPaytmApiModelListener listener, Context context) {
        return new PaytmApiModelImpl(listener, context);
    }

    public void initPaytmTransaction(GenerateOrderResponse generateOrderResponse) {
        PaytmPGService paytmPGService = PaytmPGService.getStagingService();

        if (BuildConfig.DEBUG) {
            com.jeeva.android.Log.d(TAG, "Debug enabled...");
            if (mContext != null) {
                paytmPGService.enableLog(mContext);
            }

        } else {
            com.jeeva.android.Log.d(TAG, "Production paytm servicec...");
             /* NOTE: Production service results into real transaction which results into originally payment done. * */
            paytmPGService = PaytmPGService.getProductionService();
        }

        PaytmOrder order = new PaytmOrder(getParams(generateOrderResponse));
        paytmPGService.initialize(order, null);

        com.jeeva.android.Log.d(TAG, "Starting Paytm Transaction ...");
        paytmPGService.startPaymentTransaction(mContext, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Log.d(TAG, "UI Error Occurred : MSG - " + inErrorMessage);
                        if (mListener != null) {
                            mListener.onTransactionUiError();
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.d(TAG, "Network not available");
                        if (mListener != null) {
                            mListener.onTransactionNoNetwork();
                        }
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Log.d(TAG, "Client authentication failed : MSG - " + inErrorMessage);
                        if (mListener != null) {
                            mListener.onTransactionClientAuthenticationFailed();
                        }
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        Log.d(TAG, "Error Loading page : MSG - " + iniErrorCode +  " - " + inErrorMessage);
                        if (mListener != null) {
                            mListener.onTransactionPageLoadingError();
                        }
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        Log.d(TAG, "Back pressed cancel transaction");
                        if (mListener != null) {
                            mListener.onTransactionCancelledByBackPressed();
                        }
                    }

                    @Override
                    public void onTransactionCancel(String s, Bundle bundle) {
                        Log.d(TAG, "Transaction cancelled : " + s);
                        if (mListener != null) {
                            mListener.onTransactionCancelled();
                        }
                    }

                    @Override
                    public void onTransactionResponse(Bundle bundle) {
                        Log.d(TAG, "Paytm transaction response : " + bundle);

                        PaytmTransactionResponse transactionSuccessResponse = getPaytmSuccessResponseFromBundle(bundle);
                        if (transactionSuccessResponse != null &&
                                transactionSuccessResponse.getStatus().equals(Constants.PaytmTransactionResponseStatusValues.TRANSACTION_SUCCESS)) {

                            com.jeeva.android.Log.d(TAG, "Paytm Transaction status SUCCESS ...");
                            if (mListener != null) {
                                mListener.onTransactionSuccessResponse(transactionSuccessResponse);
                            }
                        } else {
                            com.jeeva.android.Log.d(TAG, "Paytm Transaction status NOT SUCCESS - " + transactionSuccessResponse.getStatus());
                            if (mListener != null) {
                                mListener.onTransactionFailureResponse(transactionSuccessResponse);
                            }
                        }
                    }
                });

    }

    private PaytmTransactionResponse getPaytmSuccessResponseFromBundle(Bundle bundle) {
        PaytmTransactionResponse response = null;

        if (bundle != null) {
            response = new PaytmTransactionResponse();
            response.setMid(bundle.getString(Constants.PaytmSuccessResponseParamKeys.MID));
            response.setOrderId(bundle.getString(Constants.PaytmSuccessResponseParamKeys.ORDER_ID));
            response.setTransactionId(bundle.getString(Constants.PaytmSuccessResponseParamKeys.TRANSACTION_ID));
            response.setBankTransactionId(bundle.getString(Constants.PaytmSuccessResponseParamKeys.BANK_TRANSACTION_ID));
            response.setBankName(bundle.getString(Constants.PaytmSuccessResponseParamKeys.BANK_NAME));
            response.setCheckSumHash(bundle.getString(Constants.PaytmSuccessResponseParamKeys.CHECKSUM_HASH));
            response.setCurrency(bundle.getString(Constants.PaytmSuccessResponseParamKeys.CURRENCY));
            response.setGatewayName(bundle.getString(Constants.PaytmSuccessResponseParamKeys.GATEWAY_NAME));
            response.setPaymentMode(bundle.getString(Constants.PaytmSuccessResponseParamKeys.PAYMENT_MODE));
            response.setResponseCode(bundle.getString(Constants.PaytmSuccessResponseParamKeys.RESPONSE_CODE));
            response.setResponseMessage(bundle.getString(Constants.PaytmSuccessResponseParamKeys.RESPONSE_MESSAGE));
            response.setStatus(bundle.getString(Constants.PaytmSuccessResponseParamKeys.STATUS));
            response.setTransactionDate(bundle.getString(Constants.PaytmSuccessResponseParamKeys.TRANSACTION_DATE));
            response.setTransactionAmount(bundle.getString(Constants.PaytmSuccessResponseParamKeys.TRANSACTION_AMOUNT));

        }

        return response;
    }

    private Map<String, String> getParams(GenerateOrderResponse generateOrderResponse) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(Constants.PaytmParamsKeys.REQUEST_TYPE, Constants.PaytmParamValues.REQUEST_TYPE_DEFAULT);
        paramMap.put(Constants.PaytmParamsKeys.CHANNEL_ID, Constants.PaytmParamValues.CHANNEL_ID_VALUE);
        paramMap.put(Constants.PaytmParamsKeys.INDUSTRY_TYPE_ID, Constants.PaytmParamValues.INDUSTRY_TYPE_ID_VALUE);
        paramMap.put(Constants.PaytmParamsKeys.WEBSITE, Constants.PaytmParamValues.WEBSITE_VALUE);
        paramMap.put(Constants.PaytmParamsKeys.MID, Constants.PaytmParamValues.MID_VALUE);
        paramMap.put(Constants.PaytmParamsKeys.CALLBACK_URL, Constants.PaytmParamValues.CALLBACK_URL_VALUE);
        paramMap.put(Constants.PaytmParamsKeys.EMAIL, Constants.PaytmParamValues.EMAIL_VALUE);
        paramMap.put(Constants.PaytmParamsKeys.MOBILE_NO, Constants.PaytmParamValues.MOBILE_NO_VALUE);

        /*paramMap.put(Constants.PaytmParamsKeys.CHECKSUMHASH, checkSumHash);
        paramMap.put(Constants.PaytmParamsKeys.ORDER_ID, orderId);
        paramMap.put(Constants.PaytmParamsKeys.CUST_ID, custId);
        paramMap.put(Constants.PaytmParamsKeys.TXN_AMOUNT, amount);*/

        return paramMap;
    }

    public interface OnPaytmApiModelListener {
        void onTransactionUiError();
        void onTransactionNoNetwork();
        void onTransactionClientAuthenticationFailed();
        void onTransactionPageLoadingError();
        void onTransactionCancelledByBackPressed();
        void onTransactionCancelled();
        void onTransactionSuccessResponse(PaytmTransactionResponse response);
        void onTransactionFailureResponse(PaytmTransactionResponse response);
    }
}
