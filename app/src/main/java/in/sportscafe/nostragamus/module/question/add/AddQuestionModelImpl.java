package in.sportscafe.nostragamus.module.question.add;

import android.os.Bundle;

import com.jeeva.android.ExceptionTracker;

import org.json.JSONException;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 24/03/17.
 */
public class AddQuestionModelImpl implements AddQuestionModel {

    private int mMatchId;

    private OnAddQuestionModelListener mAddQuestionModelListener;

    private AddQuestionModelImpl(OnAddQuestionModelListener listener) {
        mAddQuestionModelListener = listener;
    }

    public static AddQuestionModel newInstance(OnAddQuestionModelListener listener) {
        return new AddQuestionModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if (null != bundle) {
            mMatchId = bundle.getInt(BundleKeys.MATCH_ID);
        }
    }

    @Override
    public void saveQuestion(String question, String context, String leftOption, String rightOption, String neitherOption) {
        if (question.length() < 10) {
            mAddQuestionModelListener.onNeedMoreCharForQuestion();
            return;
        }

        if (context.length() < 20) {
            mAddQuestionModelListener.onNeedMoreCharForContext();
            return;
        }

        if (leftOption.isEmpty()) {
            mAddQuestionModelListener.onEmptyLeftOption();
            return;
        }

        if (rightOption.isEmpty()) {
            mAddQuestionModelListener.onEmptyRightOption();
            return;
        }

        AddQuestionRequest addQuestionRequest = new AddQuestionRequest();
        addQuestionRequest.setMatchId(mMatchId);
        addQuestionRequest.setQuestion(question);
        addQuestionRequest.setContext(context);
        addQuestionRequest.setLeftOption(leftOption);
        addQuestionRequest.setRightOption(rightOption);

        if (neitherOption.length() > 0) {
            addQuestionRequest.setNeitherOption(neitherOption);
        }

        UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
        QuestionSubmitBy submitBy = new QuestionSubmitBy();
        submitBy.setUserId(userInfo.getId());
        submitBy.setUserName(userInfo.getUserName());
        addQuestionRequest.setCreatedBy(submitBy);

        callAddQuestionApi(addQuestionRequest);
    }

    private void callAddQuestionApi(AddQuestionRequest addQuestionRequest) {
        if (!Nostragamus.getInstance().hasNetworkConnection()) {
            mAddQuestionModelListener.onNoInternet();
            return;
        }

        mAddQuestionModelListener.onApiCallStarted();

        MyWebService.getInstance().getSubmitQuestionRequest(addQuestionRequest)
                .enqueue(new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);

                        if (!mAddQuestionModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful()) {
                            mAddQuestionModelListener.onQuestionSaveSuccess();
                        } else {
                            mAddQuestionModelListener.onQuestionSaveFailed();
                        }
                    }
                });
    }

    public interface OnAddQuestionModelListener {

        void onApiCallStarted();

        boolean onApiCallStopped();

        void onNeedMoreCharForQuestion();

        void onNeedMoreCharForContext();

        void onEmptyLeftOption();

        void onEmptyRightOption();

        void onNoInternet();

        void onQuestionSaveSuccess();

        void onQuestionSaveFailed();
    }
}