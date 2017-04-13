package in.sportscafe.nostragamus.module.allchallenges.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.parceler.Parcels;

import java.nio.BufferUnderflowException;

import in.sportscafe.nostragamus.Constants;
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
    public void callGenerateOrder(final long userId, final long challengeId, final int configIndex) {

        final GenerateOrderRequest generateOrderRequest = new GenerateOrderRequest();
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

                                if (!TextUtils.isEmpty(generateOrderResponse.getCheckSumHash())) {
                                    com.jeeva.android.Log.d(TAG, "CheckSumHash returned - continue for paytm transaction");
                                    if (mListener != null) {
                                        mListener.makePaytmTransaction(generateOrderResponse);
                                    }

                                } else {
                                    com.jeeva.android.Log.d(TAG, "CheckSumHash Empty - continue to join free challenge");
                                    if (mListener != null) {
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(Constants.BundleKeys.JOINED_CHALLENGE_INFO,Parcels.wrap(generateOrderResponse.getJoinedChallengeInfo()));
                                        mListener.joinFreeChallenge(bundle);
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
        void joinFreeChallenge(Bundle bundle);
        void onApiFailure();
        void noInternet();
    }
}
