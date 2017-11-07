package in.sportscafe.nostragamus.module.prediction.playScreen.dataProvider;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayerPollResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPollRequest;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 19/09/17.
 */

public class PredictionPlayersPollDataProvider {

    private static final String TAG = PredictionPlayersPollDataProvider.class.getSimpleName();

    public PredictionPlayersPollDataProvider() {
    }

    public void getPlayersPoll(int questionId, int roomId, PlayersPollDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            fetchPollDetailsFromServer(questionId, roomId, listener);
        } else {
            Log.d(TAG, "No Network connection");
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void fetchPollDetailsFromServer(int questionId, int roomId,
                                            final PlayersPollDataProviderListener listener) {
        PlayersPollRequest request = new PlayersPollRequest();
        request.setQuestionId(questionId);
        request.setRoomId(roomId);

        MyWebService.getInstance().getPlayerPoll(request).enqueue(new ApiCallBack<PlayerPollResponse>() {
            @Override
            public void onResponse(Call<PlayerPollResponse> call, Response<PlayerPollResponse> response) {
                super.onResponse(call, response);

                if (response.code() == 400 && response.errorBody() != null) {
                    Log.d(TAG, "Response code 400");
                    ErrorResponse errorResponse = null;

                    try {
                        errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                    } catch (Exception e) {e.printStackTrace();}
                    if (listener != null) {
                        if (errorResponse != null) {
                            listener.onServerSentError(errorResponse.getError());
                        } else {
                            listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                        }
                    }

                } else {

                    if (response.isSuccessful() && response.body() != null) {
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
            }

            @Override
            public void onFailure(Call<PlayerPollResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.d(TAG, "Server response Failed");
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface PlayersPollDataProviderListener {
        void onData(int status, @Nullable PlayerPollResponse playersPolls);
        void onError(int status);
        void onServerSentError(String msg);
    }

}
