package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaymentCoupon;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyPaymentCouponRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyPaymentCouponResponse;
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

    private AddMoneyThoughPaymentCouponApiModelImpl(AddMoneyThroughPaymentCouponApiListener listener) {
        this.mListener = listener;
    }

    public static AddMoneyThoughPaymentCouponApiModelImpl newInstance(@NonNull AddMoneyThroughPaymentCouponApiListener listener) {
        return new AddMoneyThoughPaymentCouponApiModelImpl(listener);
    }

    public void callAddMoneyPaymentCouponApi(String couponCode) {

        AddMoneyPaymentCouponRequest request = new AddMoneyPaymentCouponRequest();
        request.setCouponCode(couponCode);

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().addMoneyPaymentCouponRequest(request).enqueue(new ApiCallBack<AddMoneyPaymentCouponResponse>() {
                @Override
                public void onResponse(Call<AddMoneyPaymentCouponResponse> call, Response<AddMoneyPaymentCouponResponse> response) {
                    super.onResponse(call, response);

                    if (response.code() == 400 && response.errorBody() != null) {
                        Log.d(TAG, "Response code 400");
                        ErrorResponse errorResponse = null;

                        try {
                            errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        } catch (Exception e) {e.printStackTrace();}
                        if (mListener != null) {
                            if (errorResponse != null) {
                                mListener.onServerSentError(errorResponse.getError());
                            } else {
                                mListener.onApiFailure();
                            }
                        }

                    } else {
                        if (response.isSuccessful()) {
                            if (mListener != null) {
                                mListener.onSuccessResponse(response.body());
                            }

                        } else {
                            mListener.onApiFailure();
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

    public interface AddMoneyThroughPaymentCouponApiListener {
        void noInternet();
        void onApiFailure();
        void onSuccessResponse(AddMoneyPaymentCouponResponse response);
        void onServerSentError(String msg);
    }
}
