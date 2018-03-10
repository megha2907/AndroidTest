package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree.CashFreeApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.VerifyPaymentResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 2/28/18.
 */

public class VerifyPaymentApiImpl {

    private static final String TAG = VerifyPaymentApiImpl.class.getSimpleName();

    private VerifyPaymentApiImpl.VerifyPaymentApiListener mListener;
    private int mVerifyRetryCounter = 1;
    private Context mContext;

    public VerifyPaymentApiImpl(VerifyPaymentApiImpl.VerifyPaymentApiListener listener, @NonNull Context context) {
        this.mListener = listener;
        this.mContext = context;
    }

    public static VerifyPaymentApiImpl newInstance(VerifyPaymentApiImpl.VerifyPaymentApiListener listener, Context context) {
        return new VerifyPaymentApiImpl(listener, context);
    }


    public void verifyPayment(final String orderId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            if (!TextUtils.isEmpty(orderId)) {

                MyWebService.getInstance().verifyPayment(orderId).enqueue(new ApiCallBack<VerifyPaymentResponse>() {
                    @Override
                    public void onResponse(Call<VerifyPaymentResponse> call, Response<VerifyPaymentResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful() && response.body() != null) {

                            VerifyPaymentResponse verifyPaymentResponse = response.body();

                            if (!TextUtils.isEmpty(verifyPaymentResponse.getPaymentStatus())) {
                                String paymentStatus = verifyPaymentResponse.getPaymentStatus();
                                switch (paymentStatus.toUpperCase()) {
                                    case Constants.VerifyPaymentStatus.CREATED:
                                        tryAgain(orderId);
                                        break;
                                    case Constants.VerifyPaymentStatus.SUCCESS:
                                        if (mListener != null) {
                                            mListener.onSuccessResponse(Constants.VerifyPaymentStatus.SUCCESS);
                                        }
                                        break;
                                    case Constants.VerifyPaymentStatus.FAILURE:
                                        if (mListener != null) {
                                            mListener.onSuccessResponse(Constants.VerifyPaymentStatus.FAILURE);
                                        }
                                        break;
                                }
                            } else {
                                Log.d(TAG, "Response null");
                                if (mListener != null) {
                                    mListener.onFailure(Constants.DataStatus.FROM_SERVER_API_FAILED);
                                }
                            }

                        } else {
                            Log.d(TAG, "Response null");
                            if (mListener != null) {
                                mListener.onFailure(Constants.DataStatus.FROM_SERVER_API_FAILED);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyPaymentResponse> call, Throwable t) {
                        super.onFailure(call, t);

                        Log.e(TAG, "Verify JoinContest Api failure");
                        if (mListener != null) {
                            mListener.onFailure(Constants.DataStatus.FROM_SERVER_API_FAILED);
                        }
                    }
                });

            } else

            {
                Log.e(TAG, "OrderId empty!");
                if (mListener != null) {
                    mListener.onFailure(-1);
                }
            }
        } else {
            if (mListener != null) {
                mListener.onFailure(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void tryAgain(final String orderId) {
        if (mVerifyRetryCounter <= 15) { // MAx 15 tries
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVerifyRetryCounter++;
                    verifyPayment(orderId);
                }
            }, 4000);   /* 4 seconds to wait before re-trying */
        } else {
            if (mListener != null) {
                mListener.onPendingResponse();
            }
        }
    }

    public interface VerifyPaymentApiListener {

        void onFailure(int dataStatus);

        void onSuccessResponse(String orderStatus);

        void onPendingResponse();
    }
}
