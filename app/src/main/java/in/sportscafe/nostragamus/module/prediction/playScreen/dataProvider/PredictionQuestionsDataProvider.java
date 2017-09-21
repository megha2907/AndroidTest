package in.sportscafe.nostragamus.module.prediction.playScreen.dataProvider;

import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionAllQuestionResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 18/09/17.
 */

public class PredictionQuestionsDataProvider {

    private static final String TAG = PredictionQuestionsDataProvider.class.getSimpleName();

    public PredictionQuestionsDataProvider() {
    }

    public void getAllQuestions(int matchId, int roomId, QuestionDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadQuestionsFromServer(matchId, roomId, listener);
        } else {
            Log.d(TAG, "No Network connection");
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void loadQuestionsFromServer(int matchId, int roomId,
                                         final QuestionDataProviderListener listener) {
        MyWebService.getInstance().getAllPredictionQuestions(matchId, roomId)
                .enqueue(new ApiCallBack<PredictionAllQuestionResponse>() {
            @Override
            public void onResponse(Call<PredictionAllQuestionResponse> call,
                                   Response<PredictionAllQuestionResponse> response) {
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
            public void onFailure(Call<PredictionAllQuestionResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface QuestionDataProviderListener {
        void onData(int status, @Nullable PredictionAllQuestionResponse questionsResponse);
        void onError(int status);
    }
}
