package in.sportscafe.nostragamus.module.prediction.editAnswer.dataProvider;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.SaveEditAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.AnswerPowerUpDto;
import in.sportscafe.nostragamus.module.prediction.editAnswer.dto.SaveEditAnswerRequest;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 29/12/17.
 */

public class SaveEditAnswerApiModelImpl {

    public SaveEditAnswerApiModelImpl() {

    }

    public void callChangeAnswerApi(int matchId, int questionId, int answerId, int roomId,
                                    PowerUp powerUp, String answerTime, boolean isMinorityOption,
                                    final EditAnswerApiListener apiListener) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            AnswerPowerUpDto answerPowerUpDto = new AnswerPowerUpDto();
            if (powerUp != null) {
                if (powerUp.getDoubler() > 0) {
                    answerPowerUpDto.setDoubler(true);
                }
                if (powerUp.getNoNegative() > 0) {
                    answerPowerUpDto.setNoNegative(true);
                }
                if (powerUp.getPlayerPoll() > 0) {
                    answerPowerUpDto.setPlayerPoll(true);
                }
            }

            SaveEditAnswerRequest saveEditAnswerRequest = new SaveEditAnswerRequest();
            saveEditAnswerRequest.setAnswerId(answerId);
            saveEditAnswerRequest.setMatchId(matchId);
            saveEditAnswerRequest.setQuestionId(questionId);
            saveEditAnswerRequest.setRoomId(roomId);
            saveEditAnswerRequest.setAnswerTime(answerTime);
            saveEditAnswerRequest.setAnswerPowerup(answerPowerUpDto);

            MyWebService.getInstance().saveEditAnswer(isMinorityOption, saveEditAnswerRequest)
                    .enqueue(new NostragamusCallBack<SaveEditAnswerResponse>() {
                @Override
                public void onResponse(Call<SaveEditAnswerResponse> call, Response<SaveEditAnswerResponse> response) {
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

    public interface EditAnswerApiListener {
        void noInternet();
        void onEditAnswerSuccessful(SaveEditAnswerResponse response);
        void onApiFailure();
        void onServerSentApi(String errorMsg);
    }

}
