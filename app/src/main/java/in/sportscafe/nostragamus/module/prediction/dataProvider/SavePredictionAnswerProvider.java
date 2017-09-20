package in.sportscafe.nostragamus.module.prediction.dataProvider;

import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.prediction.dto.AnswerPowerUpDto;
import in.sportscafe.nostragamus.module.prediction.dto.AnswerRequest;
import in.sportscafe.nostragamus.module.prediction.dto.AnswerResponse;
import in.sportscafe.nostragamus.module.prediction.dto.PowerUp;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 19/09/17.
 */

public class SavePredictionAnswerProvider {

    private static final String TAG = SavePredictionAnswerProvider.class.getSimpleName();

    public SavePredictionAnswerProvider() {
    }

    public void savePredictionAnswer(PowerUp powerUp, int roomId, int matchId, int questionId, int answerId, String answerTime,
                                     boolean isMatchCompleted, boolean isMinorityOption,
                                     SavePredictionAnswerListener listener) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadQuestionsFromServer(powerUp, roomId, matchId, questionId, answerId, answerTime,
                    isMatchCompleted, isMinorityOption ,listener);

        } else {
            Log.d(TAG, "No Network connection");
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void loadQuestionsFromServer(PowerUp powerUp,
            int roomId, int matchId, int questionId, int answerId, String answerTime,
                                         boolean isMatchCompleted, boolean isMinorityOption,
                                         final SavePredictionAnswerListener listener) {

        AnswerPowerUpDto powerUpDto = new AnswerPowerUpDto();
        if (powerUp != null) {
            if (powerUp.getDoubler() > 0) {
                powerUpDto.setDoubler(true);
            }
            if (powerUp.getNoNegative() > 0) {
                powerUpDto.setNoNegative(true);
            }
            if (powerUp.getPlayerPoll() > 0) {
                powerUpDto.setPlayerPoll(true);
            }
        }

        AnswerRequest request = new AnswerRequest();
        request.setRoomId(roomId);
        request.setMatchId(matchId);
        request.setQuestionId(questionId);
        request.setAnswerId(answerId);
        request.setAnswerTime(answerTime);
        request.setPowerUp(powerUpDto);

        MyWebService.getInstance().savePredictionAnswer(request, isMatchCompleted, isMinorityOption)
                .enqueue(new ApiCallBack<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
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
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface SavePredictionAnswerListener {
        void onData(int status, @Nullable AnswerResponse answerResponse);
        void onError(int status);
    }

}
