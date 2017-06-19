package in.sportscafe.nostragamus.module.allchallenges.join;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.join.dto.JoinChallengeRequest;
import in.sportscafe.nostragamus.module.allchallenges.join.dto.JoinChallengeResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 16/06/17.
 */

public class JoinChallengeUseWalletApiModelImpl {

    private static final String TAG = JoinChallengeUseWalletApiModelImpl.class.getSimpleName();

    private JoinChallengeUseWalletApiListener mListener;

    private JoinChallengeUseWalletApiModelImpl(JoinChallengeUseWalletApiListener listener) {
        this.mListener = listener;
    }

    public static JoinChallengeUseWalletApiModelImpl newInstance(@NonNull JoinChallengeUseWalletApiListener listener) {
        return new JoinChallengeUseWalletApiModelImpl(listener);
    }

    public void makeApiCall(int challengeId, int configIndex) {

        JoinChallengeRequest request = new JoinChallengeRequest();
        request.setChallengeId(challengeId);
        request.setConfigIndex(configIndex);

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().joinChallenge(request).enqueue(new NostragamusCallBack<JoinChallengeResponse>() {
                @Override
                public void onResponse(Call<JoinChallengeResponse> call, Response<JoinChallengeResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JoinChallengeResponse joinChallengeResponse = response.body();

                        if (mListener != null) {
                            mListener.onSuccessResponse(joinChallengeResponse);
                        }
                    } else {
                        Log.d(TAG, "Api response empty!");
                        if (mListener != null) {
                            mListener.onNoApiResponse();
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

    public interface JoinChallengeUseWalletApiListener {
        void noInternet();
        void onNoApiResponse();
        void onSuccessResponse(JoinChallengeResponse joinChallengeResponse);
    }
}
