package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyPaymentCouponRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyPaymentCouponResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.VerifyPaymentCouponRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.VerifyPaymentCouponResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 28/11/17.
 */

public class AddMoneyThoughPaymentCouponApiModelImpl {
    private static final String TAG = AddMoneyThoughPaymentCouponApiModelImpl.class.getSimpleName();

    private AddMoneyThroughPaymentCouponApiListener mListener;
    private int mRetryCounter = 1;

    private AddMoneyThoughPaymentCouponApiModelImpl(AddMoneyThroughPaymentCouponApiListener listener) {
        this.mListener = listener;
    }

    public static AddMoneyThoughPaymentCouponApiModelImpl newInstance(@NonNull AddMoneyThroughPaymentCouponApiListener listener) {
        return new AddMoneyThoughPaymentCouponApiModelImpl(listener);
    }

    public void callAddMoneyPaymentCouponApi(final String couponCode) {

        AddMoneyPaymentCouponRequest request = new AddMoneyPaymentCouponRequest();
        request.setCouponCode(couponCode);

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().addMoneyPaymentCouponRequest(request).enqueue(new ApiCallBack<AddMoneyPaymentCouponResponse>() {
                @Override
                public void onResponse(Call<AddMoneyPaymentCouponResponse> call, Response<AddMoneyPaymentCouponResponse> response) {
                    super.onResponse(call, response);

                    if (response.code() == 400 && response.errorBody() != null) {
                        Log.d(TAG, "Response code 400");
                        try {
                            onResponseCode400(response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (response.isSuccessful() && response.body() != null) {
                            AddMoneyPaymentCouponResponse paymentCouponResponse = response.body();

                            if (!TextUtils.isEmpty(paymentCouponResponse.getError())) {
                                if (mListener != null) {
                                    mListener.onServerSentError(paymentCouponResponse.getError());
                                }
                            } else {
                               if (!TextUtils.isEmpty(paymentCouponResponse.getOrderId())) {
                                   verifyCouponOrder(paymentCouponResponse.getOrderId());

                               } else {
                                   Log.d(TAG, "OrderId for payment code empty!!");
                                   if (mListener != null) {
                                       mListener.onApiFailure();
                                   }
                               }
                            }

                        } else {
                            if (mListener != null) {
                                mListener.onApiFailure();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddMoneyPaymentCouponResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    if (mListener != null) {
                        mListener.onApiFailure();
                    }
                }
            });
        } else {
            if (mListener != null) {
                mListener.noInternet();
            }
        }
    }

    /**
     * Once Orderid receive, verify that order has been succeeded
     * @param orderId
     */
    private void verifyCouponOrder(final String orderId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            if (!TextUtils.isEmpty(orderId)) {
                VerifyPaymentCouponRequest request = new VerifyPaymentCouponRequest();
                request.setOrderId(orderId);

                MyWebService.getInstance().verifyPaymentCouponMoneyAdded(request).enqueue(new ApiCallBack<VerifyPaymentCouponResponse>() {
                    @Override
                    public void onResponse(Call<VerifyPaymentCouponResponse> call, Response<VerifyPaymentCouponResponse> response) {
                        super.onResponse(call, response);

                        if (response.code() == 400 && response.errorBody() != null) {
                            Log.d(TAG, "Response code 400");
                            try {
                                onResponseCode400(response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (response.isSuccessful() && response.body() != null) {
                                VerifyPaymentCouponResponse verifyResponse = response.body();

                                if (!TextUtils.isEmpty(verifyResponse.getError())) {
                                    if (mListener != null) {
                                        mListener.onServerSentError(verifyResponse.getError());
                                    }
                                } else if (verifyResponse.isTryAgain()) {

                                    tryAgain(orderId);

                                } else {
                                    if (mListener != null) {
                                        mListener.onSuccessResponse(verifyResponse);
                                    }
                                }

                            } else {
                                if (mListener != null) {
                                    mListener.onApiFailure();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyPaymentCouponResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d(TAG, "Verify payment coupon failed");
                        if (mListener != null) {
                            mListener.onApiFailure();
                        }
                    }
                });
            } else {
                if (mListener != null) {
                    mListener.noInternet();
                }
            }
        } else {
            if (mListener != null) {
                mListener.noInternet();
            }
        }
    }

    private void onResponseCode400(String errorResponseStr) {
        ErrorResponse errorResponse = null;

        try {
            errorResponse = new Gson().fromJson(errorResponseStr, ErrorResponse.class);
        } catch (Exception e) {e.printStackTrace();}
        if (mListener != null) {
            if (errorResponse != null) {
                mListener.onServerSentError(errorResponse.getError());
            } else {
                mListener.onApiFailure();
            }
        }
    }

    private void tryAgain(final String orderId) {
        if (mRetryCounter <= 15) { // Max 15 tries
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRetryCounter++;
                    verifyCouponOrder(orderId);
                }
            }, 4000);   /* 4 seconds to wait before re-trying */
        } else {
            if (mListener != null) {
                mListener.onApiFailure();
            }
        }
    }

    public interface AddMoneyThroughPaymentCouponApiListener {
        void noInternet();
        void onApiFailure();
        void onSuccessResponse(VerifyPaymentCouponResponse response);
        void onServerSentError(String msg);
    }
}
