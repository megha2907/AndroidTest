package in.sportscafe.nostragamus.module.contest.dataProvider;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesRequest;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestEntriesDataProvider {

    private static final String TAG = ContestEntriesDataProvider.class.getSimpleName();

    public ContestEntriesDataProvider() {}

    public void getContestEntries(int challengeId, int contestId, @NonNull ContestEntriesDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadContestEntries(challengeId, contestId, listener);
        } else {
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void loadContestEntries(int challengeId, int contestId, final ContestEntriesDataProviderListener listener) {

        MyWebService.getInstance().getContestEntries(contestId).enqueue(new ApiCallBack<ContestEntriesResponse>() {
            @Override
            public void onResponse(Call<ContestEntriesResponse> call, Response<ContestEntriesResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onSuccessResponse(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                    if (listener != null) {
                        listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ContestEntriesResponse> call, Throwable t) {
                super.onFailure(call, t);
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });

    }

    public interface ContestEntriesDataProviderListener {
        void onSuccessResponse(int status, ContestEntriesResponse response);
        void onError(int status);
    }
}
