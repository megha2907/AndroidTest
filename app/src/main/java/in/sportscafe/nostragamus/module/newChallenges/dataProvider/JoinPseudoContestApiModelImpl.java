package in.sportscafe.nostragamus.module.newChallenges.dataProvider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.newChallenges.dto.JoinPseudoContestRequest;
import in.sportscafe.nostragamus.module.newChallenges.dto.JoinPseudoContestResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 23/09/17.
 */

public class JoinPseudoContestApiModelImpl {

    private static final String TAG = JoinPseudoContestApiModelImpl.class.getSimpleName();

    public JoinPseudoContestApiModelImpl() {
    }

    public void joinPseudoContest(int challengeId, @NonNull JoinPseudoContestApiListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callJoinPseudoContestApi(challengeId, listener);
        } else {
            Log.d(TAG, "No Network connection");
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void callJoinPseudoContestApi(int challengeId, final JoinPseudoContestApiListener listener) {
        JoinPseudoContestRequest request = new JoinPseudoContestRequest();
        request.setChallengeId(challengeId);

        MyWebService.getInstance().joinPseudoContest(request).enqueue(new ApiCallBack<JoinPseudoContestResponse>() {
            @Override
            public void onResponse(Call<JoinPseudoContestResponse> call, Response<JoinPseudoContestResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                    if (listener != null) {
                        listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<JoinPseudoContestResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed - " + t.getMessage());
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface JoinPseudoContestApiListener {
        void onData(int status, @Nullable JoinPseudoContestResponse responses);
        void onError(int status);
    }
}
