package in.sportscafe.nostragamus.module.play.prediction;

import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.AnswerIds;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 30/6/16.
 */
public class PostAnswerModelImpl {

    private PostAnswerModelListener mPostAnswerModelListener;

    private PostAnswerModelImpl(PostAnswerModelListener listener) {
        this.mPostAnswerModelListener = listener;
    }

    public static PostAnswerModelImpl newInstance(PostAnswerModelListener listener) {
        return new PostAnswerModelImpl(listener);
    }

    public void postAnswer(Question question, int challengeId, boolean matchComplete) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            Answer answer = new Answer(
                    question.getMatchId(),
                    question.getQuestionId(),
                    question.getAnswerId(),
                    TimeUtils.getCurrentTime(DateFormats.FORMAT_DATE_T_TIME_ZONE, DateFormats.GMT),
                    question.getPowerUpId(),
                    challengeId
            );
            callPostAnswerApi(answer, question.isMinorityAnswer(), matchComplete);
        } else {
            mPostAnswerModelListener.onNoInternet();
        }
    }

    private void callPostAnswerApi(final Answer answer, boolean minorityOption, boolean matchComplete) {
        MyWebService.getInstance().getPostAnswerRequest(answer, matchComplete, minorityOption).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            String responseMsg = response.body().getMessage();
                            if (responseMsg.equals("Match has already started")) {
                                mPostAnswerModelListener.onMatchAlreadyStarted();
                            } else {
                                handlePostAnswerResponse(answer);
                            }
                        } else {
                            mPostAnswerModelListener.onFailed(response.message());
                        }
                    }
                }
        );
    }

    private void handlePostAnswerResponse(Answer answer) {
        String powerupId = answer.getPowerUpId();
        if (null != powerupId) {
            NostragamusAnalytics.getInstance().trackPowerups(AnalyticsActions.APPLIED, powerupId);
        }

        mPostAnswerModelListener.onSuccess(getAnswerDirection(answer.getAnswerId()));
    }

    private String getAnswerDirection(int answerId) {
        switch (answerId) {
            case AnswerIds.LEFT:
                return AnalyticsLabels.LEFT;
            case AnswerIds.RIGHT:
                return AnalyticsLabels.RIGHT;
            case AnswerIds.NEITHER:
                return AnalyticsLabels.TOP;
            default:
                return AnalyticsLabels.BOTTOM;
        }
    }

    public interface PostAnswerModelListener {

        void onSuccess(String answerDirection);

        void onNoInternet();

        void onFailed(String message);

        void onMatchAlreadyStarted();
    }
}