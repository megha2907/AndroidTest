package in.sportscafe.nostragamus.module.challengeCompleted.dataProvider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedMatchesResponse;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayMatchesDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedChallengeMatchesDataProvider {

    private static final String TAG = InPlayMatchesDataProvider.class.getSimpleName();

    public CompletedChallengeMatchesDataProvider() {
    }

    public void getCompletedChallengeMatches(int roomId, int challlengeId, @NonNull CompletedChallengeMatchesDataProvider.CompletedChallengeMatchesDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadCompletedChallengeMatchesDataFromServer(roomId, challlengeId, listener);
        } else {
            Log.d(TAG, "No Network connection");
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void loadCompletedChallengeMatchesDataFromServer(int roomId, int challengeId, final CompletedChallengeMatchesDataProvider.CompletedChallengeMatchesDataProviderListener listener) {
        MyWebService.getInstance().getCompletedChallengeMatches(roomId, challengeId).enqueue(new ApiCallBack<CompletedMatchesResponse>() {
            @Override
            public void onResponse(Call<CompletedMatchesResponse> call, Response<CompletedMatchesResponse> response) {
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
            public void onFailure(Call<CompletedMatchesResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed - " + t.getMessage());
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface CompletedChallengeMatchesDataProviderListener {
        void onData(int status, @Nullable CompletedMatchesResponse responses);
        void onError(int status);
    }
}
