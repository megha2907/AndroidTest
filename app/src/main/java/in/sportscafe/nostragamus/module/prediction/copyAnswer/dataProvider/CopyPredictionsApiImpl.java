package in.sportscafe.nostragamus.module.prediction.copyAnswer.dataProvider;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerRequest;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 25/1/18.
 */

public class CopyPredictionsApiImpl {

    private static final String TAG = CopyPredictionsApiImpl.class.getSimpleName();

    public void useContestAndCopyPredictions(int matchId, int sourceRoomId,
                                             int targetRoomId, boolean shouldUsePowerUps,
                                             @NonNull final CopyAnswerUsePredictionsApiListener listener) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            CopyAnswerRequest request = new CopyAnswerRequest();
            request.setMatchId(matchId);
            request.setSourceRoomId(sourceRoomId);
            request.setTargetRoomId(targetRoomId);
            request.setShouldCopyPowerups(shouldUsePowerUps);

            MyWebService.getInstance().copyAnswer(request).enqueue(new ApiCallBack<CopyAnswerResponse>() {
                        @Override
                        public void onResponse(Call<CopyAnswerResponse> call, Response<CopyAnswerResponse> response) {
                            super.onResponse(call, response);

                            if (listener != null) {
                                if (response.code() == 400 && response.errorBody() != null) {

                                    ErrorResponse errorResponse = null;
                                    try {
                                        errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (errorResponse != null) {
                                        listener.onServerSentError(errorResponse.getError());
                                    } else {
                                        listener.onApiFailure();
                                    }

                                } else if (response.isSuccessful() && response.body() != null) {
                                    listener.onSuccess(response.body());

                                } else {
                                    listener.onApiFailure();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CopyAnswerResponse> call, Throwable t) {
                            super.onFailure(call, t);
                            Log.d(TAG, "Api failure");
                            if (listener != null) {
                                listener.onApiFailure();
                            }
                        }
                    });

        } else {
            if (listener != null) {
                listener.noInternet();
            }
        }
    }

    public interface CopyAnswerUsePredictionsApiListener {
        void noInternet();
        void onSuccess(CopyAnswerResponse response);
        void onApiFailure();
        void onServerSentError(String errorMsg);
    }
}
