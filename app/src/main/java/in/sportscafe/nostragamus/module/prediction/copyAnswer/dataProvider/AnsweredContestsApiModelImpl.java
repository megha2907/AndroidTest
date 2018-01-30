package in.sportscafe.nostragamus.module.prediction.copyAnswer.dataProvider;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContestsResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerRequest;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 24/1/18.
 */

public class AnsweredContestsApiModelImpl {

    public void fetchCopyAnswerContestsList(int matchId,
                                            @NonNull final CopyAnswerContestApiListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().getCopyAnswerContestList(matchId).
                    enqueue(new ApiCallBack<CopyAnswerContestsResponse>() {
                @Override
                public void onResponse(Call<CopyAnswerContestsResponse> call, Response<CopyAnswerContestsResponse> response) {
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
                            listener.onCopyAnswerContestListReceived(response.body());

                        } else {
                            listener.onApiFailure();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CopyAnswerContestsResponse> call, Throwable t) {
                    super.onFailure(call, t);

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

    public interface CopyAnswerContestApiListener {
        void noInternet();
        void onCopyAnswerContestListReceived(CopyAnswerContestsResponse response);
        void onApiFailure();
        void onServerSentError(String errorMsg);
    }
}
