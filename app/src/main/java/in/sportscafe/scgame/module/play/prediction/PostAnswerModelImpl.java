package in.sportscafe.scgame.module.play.prediction;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.play.prediction.dto.Answer;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 30/6/16.
 */
public class PostAnswerModelImpl {

    private PostAnswerModelListener mPostAnswerModelListener;

    public PostAnswerModelImpl(PostAnswerModelListener modelListener) {
        this.mPostAnswerModelListener = modelListener;
    }

    public void postAnswer(Answer answer) {
        if (ScGame.getInstance().hasNetworkConnection()) {
            callPostAnswerApi(answer);
        } else {
            mPostAnswerModelListener.onNoInternet();
        }
    }

    private void callPostAnswerApi(Answer answer) {
        MyWebService.getInstance().getPostAnswerRequest(answer).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            mPostAnswerModelListener.onSuccess();
                        } else {
                            mPostAnswerModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface PostAnswerModelListener {

        void onSuccess();

        void onNoInternet();

        void onFailed(String message);
    }
}