package in.sportscafe.nostragamus.module.inPlay.dataProvider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchRequest;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchesDataProvider {

    private static final String TAG = InPlayMatchesDataProvider.class.getSimpleName();

    public InPlayMatchesDataProvider() {
    }

    public void getInPlayMatches(int contestId, @NonNull InPlayMatchesDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadInPlayMatchesDataFromServer(contestId, listener);
        } else {
            Log.d(TAG, "No Network connection");
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void loadInPlayMatchesDataFromServer(int contestId, final InPlayMatchesDataProviderListener listener) {

        InPlayMatchRequest request = new InPlayMatchRequest();
        request.setContestId(contestId);

        MyWebService.getInstance().getInPlayMatches(request).enqueue(new ApiCallBack<InPlayMatchesResponse>() {
            @Override
            public void onResponse(Call<InPlayMatchesResponse> call, Response<InPlayMatchesResponse> response) {
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
            public void onFailure(Call<InPlayMatchesResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed - " + t.getMessage());
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface InPlayMatchesDataProviderListener {
        void onData(int status, @Nullable InPlayMatchesResponse responses);
        void onError(int status);
    }

}
