package in.sportscafe.nostragamus.module.newChallenges.dataProvider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 09/09/17.
 */

public class NewChallengesMatchesDataProvider {

    private static final String TAG = NewChallengesMatchesDataProvider.class.getSimpleName();

    public NewChallengesMatchesDataProvider() {
    }

    public void getNewChallengesMatches(int challengeId, @NonNull NewChallengesApiListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadInPlayMatchesDataFromServer(challengeId, listener);
        } else {
            Log.d(TAG, "No Network connection");
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void loadInPlayMatchesDataFromServer(int challengeId, final NewChallengesApiListener listener) {
        MyWebService.getInstance().getNewChallengeMatches(challengeId).enqueue(new ApiCallBack<NewChallengeMatchesResponse>() {
            @Override
            public void onResponse(Call<NewChallengeMatchesResponse> call, Response<NewChallengeMatchesResponse> response) {
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
            public void onFailure(Call<NewChallengeMatchesResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed - " + t.getMessage());
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface NewChallengesApiListener {
        void onData(int status, @Nullable NewChallengeMatchesResponse responses);
        void onError(int status);
    }

}
