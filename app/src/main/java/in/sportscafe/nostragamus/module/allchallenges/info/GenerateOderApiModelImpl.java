package in.sportscafe.nostragamus.module.allchallenges.info;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.paytm.GenerateOrderRequest;
import in.sportscafe.nostragamus.module.paytm.GenerateOrderResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 10/04/17.
 */

public class GenerateOderApiModelImpl {

    private static final String TAG = GenerateOderApiModelImpl.class.getSimpleName();

    private GenerateOderApiModelImpl.OnGenerateOrderApiModelListener mListener;

    private GenerateOderApiModelImpl(GenerateOderApiModelImpl.OnGenerateOrderApiModelListener listener) {
        this.mListener = listener;
    }

    public static GenerateOderApiModelImpl newInstance(@NonNull GenerateOderApiModelImpl.OnGenerateOrderApiModelListener listener) {
        return new GenerateOderApiModelImpl(listener);
    }

    /**
     * Generates orderId & returns Paytm params along with checkSumHash to make transaction for paid challenges
     * If checkSumHash is null then it's free challenge, continue joining...
     * @param userId - userId (CustId)
     * @param challengeId - challengeId
     * @param configIndex - configIndex
     */
    public void callGenerateOrder(final String userId, String challengeId, String configIndex) {

        GenerateOrderRequest generateOrderRequest = new GenerateOrderRequest();
        generateOrderRequest.setUserId(userId);
        generateOrderRequest.setChallengeId(challengeId);
        generateOrderRequest.setConfigIndex(configIndex);

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getGenerateOrderRequest(generateOrderRequest)
                    .enqueue(new NostragamusCallBack<GenerateOrderResponse>() {
                        @Override
                        public void onResponse(Call<GenerateOrderResponse> call, Response<GenerateOrderResponse> response) {
                            super.onResponse(call, response);

                            if (response != null && response.isSuccessful() && response.body() != null) {
                                GenerateOrderResponse generateOrderResponse = response.body();

                                if (!TextUtils.isEmpty(generateOrderResponse.getCHECKSUMHASH())) {
                                    com.jeeva.android.Log.d(TAG, "CheckSumHash returned - continue for paytm transaction");
                                    if (mListener != null) {
                                        mListener.makePaytmTransaction(generateOrderResponse);
                                    }

                                } else {
                                    com.jeeva.android.Log.d(TAG, "CheckSumHash Empty");
                                    if (mListener != null) {
                                        mListener.joinFreeChallenge();
                                    }
                                }

                            } else {
                                com.jeeva.android.Log.d(TAG, "Generate order response not successful / null");
                                if (mListener != null) {
                                    mListener.onApiFailure();
                                }
                            }
                        }
                    });
        } else {
            if (mListener != null) {
                mListener.noInternet();
            }
        }
    }

    public interface OnGenerateOrderApiModelListener {
        void makePaytmTransaction(GenerateOrderResponse generateOrderResponse);
        void joinFreeChallenge();
        void onApiFailure();
        void noInternet();
    }
}
