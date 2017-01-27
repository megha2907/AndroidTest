package in.sportscafe.nostragamus.module.play.prediction;

import com.jeeva.android.Log;

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

    public void postAnswer(Answer answer, boolean minorityOption, Boolean matchComplete) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callPostAnswerApi(answer, minorityOption, matchComplete);
        } else {
            mPostAnswerModelListener.onNoInternet();
        }
    }

    private void callPostAnswerApi(Answer answer, boolean minorityOption, Boolean matchComplete) {
        MyWebService.getInstance().getPostAnswerRequest(answer, minorityOption, matchComplete).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("Match has already started")) {
                                mPostAnswerModelListener.onFailed(response.body().getMessage());
                            } else {
                                mPostAnswerModelListener.onSuccess();
                                Log.i("inside", "onSuccess");
                            }
                        } else {
                            Log.i("inside", "onFailed");
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