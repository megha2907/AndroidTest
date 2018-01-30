package in.sportscafe.nostragamus.module.prediction.editAnswer.dataProvider;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.QuestionForEditAnswerResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 29/12/17.
 */

public class GetQuestionForEditAnswerApiModelImpl {

    public void fetchQuestionDetailsToEditAnswers(int matchId, int questionId, int roomId,
                                                  final GetQuestionForEditAnswerApiListener apiListener) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().questionForEditAnswer(roomId, matchId, questionId).
                    enqueue(new NostragamusCallBack<QuestionForEditAnswerResponse>() {
                        @Override
                        public void onResponse(Call<QuestionForEditAnswerResponse> call, Response<QuestionForEditAnswerResponse> response) {
                            super.onResponse(call, response);

                            if (apiListener != null) {
                                if (response.code() == 400 && response.errorBody() != null) {

                                    ErrorResponse errorResponse = null;
                                    try {
                                        errorResponse = MyWebService.getInstance().getObjectFromJson(response.errorBody().string(), ErrorResponse.class);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (errorResponse != null) {
                                        apiListener.onServerSentApi(errorResponse.getError());
                                    } else {
                                        apiListener.onApiFailure();
                                    }

                                } else if (response.isSuccessful() && response.body() != null) {
                                    apiListener.onEditAnswerSuccessful(response.body());

                                } else {
                                    apiListener.onApiFailure();
                                }
                            }
                        }
                    });
        } else {
            if (apiListener != null) {
                apiListener.noInternet();
            }
        }
    }

    public interface GetQuestionForEditAnswerApiListener {
        void noInternet();
        void onEditAnswerSuccessful(QuestionForEditAnswerResponse response);
        void onApiFailure();
        void onServerSentApi(String errorMsg);
    }
}
