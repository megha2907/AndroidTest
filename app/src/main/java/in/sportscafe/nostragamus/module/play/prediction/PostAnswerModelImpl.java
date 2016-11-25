package in.sportscafe.nostragamus.module.play.prediction;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
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
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callPostAnswerApi(answer);
        } else {
            mPostAnswerModelListener.onNoInternet();
        }
    }

    private void callPostAnswerApi(Answer answer) {
        MyWebService.getInstance().getPostAnswerRequest(answer).enqueue(
                new NostragamusCallBack<ApiResponse>() {
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