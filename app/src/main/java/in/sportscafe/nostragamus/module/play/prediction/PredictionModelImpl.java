package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.offline.PredictionDataHandler;
import in.sportscafe.nostragamus.module.play.prediction.dto.Answer;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePoll;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollRequest;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.HEAD;

import static in.sportscafe.nostragamus.Constants.BundleKeys;
import static in.sportscafe.nostragamus.Constants.DateFormats;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PredictionModelImpl implements PredictionModel,
        SwipeFlingAdapterView.OnSwipeListener<Question> {

    private boolean mDummyGame = false;

    private PredictionAdapter mPredictionAdapter;

    private OnPredictionModelListener mPredictionModelListener;

    private Match mMyResult;

    private int mTotalCount;

    private int minitialCount;

    private Integer matchId;

    private int mRemainingTime;

    private String sportName;

    private Question mTopQuestion;

    private String majorityAnswer;

    private String minorityAnswer;

    private Boolean isMinorityOption = false;

    private boolean mNeitherOptionAvailable = false;

    private Boolean matchComplete = false;

    public PredictionModelImpl(OnPredictionModelListener predictionModelListener) {
        this.mPredictionModelListener = predictionModelListener;
    }

    public static PredictionModel newInstance(OnPredictionModelListener predictionModelListener) {
        return new PredictionModelImpl(predictionModelListener);
    }

    @Override
    public void saveData(Bundle bundle) {

        if (!bundle.containsKey(BundleKeys.IS_DUMMY_GAME)) {
            mMyResult = (Match) bundle.getSerializable(BundleKeys.MATCH_LIST);
            matchId = mMyResult.getId();

            if (null != bundle.getString(BundleKeys.SPORT_NAME)) {
                mPredictionModelListener.onGetSportName(bundle.getString(BundleKeys.SPORT_NAME));
            }

            getAllQuestions();
        } else {
            mDummyGame = bundle.getBoolean(BundleKeys.IS_DUMMY_GAME);


        }
    }

    private Match getDummyGameMatch() {
        String dummyMatchJson = "";
        return MyWebService.getInstance().getObjectFromJson(dummyMatchJson, Match.class);
    }

    @Override
    public boolean isDummyGame() {
        return mDummyGame;
    }

    @Override
    public void getAllQuestions() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callAllQuestionsApi(matchId);
        } else {
            mPredictionModelListener.onNoInternet();
        }
    }

    private void callAllQuestionsApi(Integer matchId) {
        MyWebService.getInstance().getAllQuestions(matchId).enqueue(new NostragamusCallBack<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                super.onResponse(call, response);
                if (null == mPredictionModelListener.getContext()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<Question> allQuestions = response.body().getQuestions();

                    if (null == allQuestions || allQuestions.isEmpty() || allQuestions.size() < 0) {
                        mPredictionModelListener.onNoQuestions();

                    } else {
                        populateAdapterData(allQuestions);
                        mPredictionModelListener.onSuccessQuestions();
                    }
                } else {
                    mPredictionModelListener.onFailedQuestions(response.message());
                }
            }
        });
    }

    private void populateAdapterData(List<Question> questionList) {
        mTotalCount = questionList.size();
        minitialCount = questionList.size();

        mPredictionAdapter = new PredictionAdapter(mPredictionModelListener.getContext(),
                mPredictionModelListener.getRootView());
        mPredictionModelListener.onGetAdapter(mPredictionAdapter, this);
        int count = 1;
        for (Question question : questionList) {
            question.setQuestionNumber(count++);
            mPredictionAdapter.add(question);
        }

        mPredictionAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePowerUps(String powerup) {

        if (powerup.equals("2x")) {

            mPredictionAdapter.update2xPowerUp();
        } else if (powerup.equals("no_negs")) {

            mPredictionAdapter.updateNonegsPowerUp();
        } else if (powerup.equals("player_poll")) {
            AudiencePollRequest audiencePollRequest = new AudiencePollRequest();
            audiencePollRequest.setQuestionId(mTopQuestion.getQuestionId());
            callAudiencePollApi(audiencePollRequest);
        }
        mPredictionAdapter.notifyDataSetChanged();
        mPredictionModelListener.onNegativePowerUpApplied();
    }

    private void callAudiencePollApi(AudiencePollRequest request) {

        MyWebService.getInstance().getAudiencePoll(request).enqueue(new NostragamusCallBack<AudiencePollResponse>() {
            @Override
            public void onResponse(Call<AudiencePollResponse> call, Response<AudiencePollResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {

                    if (null == response.body().getAudiencePoll()) {
                        mPredictionModelListener.onFailedAudiencePollResponse("AudiencePoll Fail");
                    } else {
                        List<AudiencePoll> audiencePoll = response.body().getAudiencePoll();
                        String answer1Percentage = audiencePoll.get(0).getAnswerPercentage();
                        String answer2Percentage = audiencePoll.get(1).getAnswerPercentage();

                        if (Integer.parseInt(answer1Percentage.replaceAll("%", "")) > Integer.parseInt(answer2Percentage.replaceAll("%", ""))) {
                            majorityAnswer = mTopQuestion.getQuestionOption1();
                            minorityAnswer = mTopQuestion.getQuestionOption2();
                        } else {
                            majorityAnswer = mTopQuestion.getQuestionOption2();
                            minorityAnswer = mTopQuestion.getQuestionOption1();
                        }

                        mPredictionAdapter.updateAudiencePollPowerUp(answer1Percentage, answer2Percentage);
                        mPredictionAdapter.notifyDataSetChanged();
                        mPredictionModelListener.onNegativePowerUpApplied();
                        mPredictionModelListener.onSuccessAudiencePollResponse();
                    }

                } else {
                    mPredictionModelListener.onFailedAudiencePollResponse(response.message());
                }
            }
        });

    }

    @Override
    public void setFlingCardListener(FlingCardListener flingCardListener) {
        mPredictionAdapter.setFlingCardListener(flingCardListener);
    }

    @Override
    public String getTournamentName() {
        return mMyResult.getTournamentName();
    }

    @Override
    public String getMatchStage() {
        return mMyResult.getStage();
    }

    @Override
    public String getTournamentPhoto() {
        return mMyResult.getTournamentPhoto();
    }

    @Override
    public String getContestName() {
        return String.valueOf(mMyResult.getParties().get(0).getPartyName() + "  vs  " + mMyResult.getParties().get(1).getPartyName());
    }

    @Override
    public void removeFirstObjectInAdapter(Question dataObject) {
        mPredictionAdapter.remove(dataObject);
        mPredictionAdapter.notifyDataSetChanged();
        mPredictionModelListener.dismissPowerUpApplied();

        if (dataObject.getPowerUpId() != null) {
            if (dataObject.getPowerUpId().equalsIgnoreCase("2x")) {
                NostragamusDataHandler.getInstance().setNumberof2xPowerups(NostragamusDataHandler.getInstance().getNumberof2xPowerups() - 1);
            } else if (dataObject.getPowerUpId().equalsIgnoreCase("player_poll")) {
                NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() - 1);
            } else if (dataObject.getPowerUpId().equalsIgnoreCase("no_negs")) {
                NostragamusDataHandler.getInstance().setNumberofNonegsPowerups(NostragamusDataHandler.getInstance().getNumberofNonegsPowerups() - 1);
            }
        }
    }

    @Override
    public void onLeftSwipe(Question dataObject) {

        if (null != majorityAnswer) {
            if (!majorityAnswer.equals(dataObject.getQuestionOption1())) {
                isMinorityOption = true;
            }
        }
        saveSinglePrediction(dataObject, 1, isMinorityOption, matchComplete);
    }

    @Override
    public void onRightSwipe(Question dataObject) {
        if (null != majorityAnswer) {
            if (!majorityAnswer.equals(dataObject.getQuestionOption2())) {
                isMinorityOption = true;
            }
        }

        saveSinglePrediction(dataObject, 2, isMinorityOption, matchComplete);
    }

    @Override
    public void onTopSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, 3, isMinorityOption, matchComplete);
    }

    @Override
    public void onBottomSwipe(Question dataObject) {
        mPredictionAdapter.add(dataObject);
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 0) {
            PredictionDataHandler.getInstance().saveMyResult(mMyResult);

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.BundleKeys.TOURNAMENT_ID, mMyResult.getTournamentId());
            bundle.putString(Constants.BundleKeys.TOURNAMENT_NAME, mMyResult.getTournamentName());
            mPredictionModelListener.onSuccessCompletion(bundle);
            return;
        }

        if (itemsInAdapter == 1 && mTotalCount == 1) {
            matchComplete = true;
            mPredictionModelListener.onShowingLastQuestion();
        }

        mTopQuestion = mPredictionAdapter.getTopQuestion();
        mNeitherOptionAvailable = !TextUtils.isEmpty(mTopQuestion.getQuestionOption3());

        mPredictionModelListener.onQuestionChanged(mTopQuestion, minitialCount, mNeitherOptionAvailable);
    }

    @Override
    public boolean needTopSwipe() {
        return mNeitherOptionAvailable;
    }

    @Override
    public boolean needBottomSwipe() {
        return mPredictionAdapter.getCount() > 1;
    }

    @Override
    public void onCardMoving(float xPercent, float yPercent) {
        Log.d("Move Percentages --> ", xPercent + "%  " + yPercent + "%");

        mPredictionAdapter.onCardMoving(xPercent, yPercent);

    }

    private void saveSinglePrediction(Question question, int answerId, boolean minorityOption, Boolean matchComplete) {
        Answer answer = new Answer(
                question.getMatchId(),
                question.getQuestionId(),
                String.valueOf(answerId),
                TimeUtils.getCurrentTime(DateFormats.FORMAT_DATE_T_TIME_ZONE, DateFormats.GMT),
                question.getPowerUpId()

        );
        postAnswerToServer(answer, minorityOption, matchComplete);
    }

    private void postAnswerToServer(Answer answer, boolean minorityOption, Boolean matchComplete) {
        new PostAnswerModelImpl(new PostAnswerModelImpl.PostAnswerModelListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onNoInternet() {
            }

            @Override
            public void onFailed(String message) {
                mPredictionModelListener.onFailedPostAnswerToServer(message);

            }
        }).postAnswer(answer, minorityOption, matchComplete);
    }

    public interface OnPredictionModelListener {

        Context getContext();

        void onGetAdapter(PredictionAdapter predictionAdapter,
                          SwipeFlingAdapterView.OnSwipeListener<Question> listener);

        void onSuccessCompletion(Bundle bundle);

        void onShowingLastQuestion();

        void onShowingPassedQuestions();

        void dismissPowerUpApplied();

        void onNoInternet();

        void onSuccessQuestions();

        void onFailedQuestions(String message);

        void onNoQuestions();

        void onQuestionChanged(Question item, int minitialCount, boolean neitherAvailable);

        void onFailedAudiencePollResponse(String message);

        void onSuccessAudiencePollResponse();

        void onNegativePowerUpApplied();

        void onGetSportName(String sportName);

        void onFailedPostAnswerToServer(String message);

        View getRootView();
    }
}