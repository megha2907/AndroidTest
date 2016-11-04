package in.sportscafe.scgame.module.play.prediction;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.offline.PredictionDataHandler;
import in.sportscafe.scgame.module.play.prediction.dto.Answer;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.prediction.dto.QuestionsResponse;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.scgame.utils.timeutils.TimeUtils;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

import static in.sportscafe.scgame.Constants.BundleKeys;
import static in.sportscafe.scgame.Constants.DateFormats;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PredictionModelImpl implements PredictionModel,
        SwipeFlingAdapterView.OnSwipeListener<Question> {

    private PredictionAdapter mPredictionAdapter;

    private OnPredictionModelListener mPredictionModelListener;


    private Map<Integer, Match> mMatchMap = new HashMap<>();

    private List<Match> mMyResultList = new ArrayList<>();

    private Match mMyResult;

    private int mTotalCount;

    private Integer matchId;

    private int mRemainingTime;

    private boolean mPassEnabled = true;

    public PredictionModelImpl(OnPredictionModelListener predictionModelListener) {
        this.mPredictionModelListener = predictionModelListener;
    }

    public static PredictionModel newInstance(OnPredictionModelListener predictionModelListener) {
        return new PredictionModelImpl(predictionModelListener);
    }

    @Override
    public void saveData(Bundle bundle) {

        //matchId = bundle.getInt(Constants.BundleKeys.MATCH_ID);
        mMyResult = (Match) bundle.getSerializable(BundleKeys.MATCH_LIST);
        matchId = mMyResult.getId();
        Log.i("tournamentname",mMyResult.getTournamentName());

        //populateAdapterData(mMyResult.getQuestions());

        getAllQuestions();
    }


    @Override
    public void getAllQuestions() {
        if (ScGame.getInstance().hasNetworkConnection()) {
            callAllQuestionsApi(matchId);
        } else {
            mPredictionModelListener.onNoInternet();
        }
    }

    private void callAllQuestionsApi(Integer matchId) {
        MyWebService.getInstance().getAllQuestions(matchId).enqueue(new ScGameCallBack<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                if (null == mPredictionModelListener.getContext()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<Question> allQuestions = response.body().getQuestions();

                    if (null == allQuestions || allQuestions.isEmpty() || allQuestions.size() < 0) {
                        mPredictionModelListener.onNoQuestions();
                        Log.i("null","questions");
                    }
                    else {
                        handleAllQuestionsResponse(allQuestions);
                    }
                } else {
                    mPredictionModelListener.onFailedQuestions(response.message());
                }
            }
        });
    }

    private void handleAllQuestionsResponse(List<Question> questionList) {
        Map<Integer, Match> myResultMap = new HashMap<>();

        Integer matchId;
        Match myResult;
        for (Question question : questionList) {
            matchId = question.getMatchId();

            if (myResultMap.containsKey(matchId)) {
                myResult = myResultMap.get(matchId);
            } else {
                myResult = mMatchMap.get(matchId);
                mMyResultList.add(mMyResult);
                myResultMap.put(matchId, myResult);
            }

            mMyResult.getQuestions().add(question);
        }

        mPredictionModelListener.onSuccessQuestions(questionList);
        myResult = mMyResultList.get(0);
        populateAdapterData(myResult.getQuestions());

    }


    private void populateAdapterData(List<Question> questionList) {
        mTotalCount = questionList.size();

        mPredictionAdapter = new PredictionAdapter(mPredictionModelListener.getContext(),
                 mTotalCount);
        mPredictionModelListener.onGetAdapter(mPredictionAdapter, this);

        int count = 1;
        for (Question question : questionList) {
            question.setQuestionNumber(count++);
            mPredictionAdapter.add(question);
        }

        mPredictionAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePowerUps() {
        mPredictionAdapter.updatePowerUp();
        mPredictionAdapter.notifyDataSetChanged();
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
        return String.valueOf(mMyResult.getParties().get(0).getPartyName()+"  vs  "+mMyResult.getParties().get(1).getPartyName());
    }

    @Override
    public void removeFirstObjectInAdapter(Question dataObject) {
       // mRemainingTime = mPredictionAdapter.getRemainingTime();
       // Log.d("Remaining Time", mRemainingTime + " seconds");

        mTotalCount--;
       // mPredictionAdapter.stopTimer();
        mPredictionAdapter.remove(dataObject);
        mPredictionAdapter.notifyDataSetChanged();
        mPredictionModelListener.dismissPowerUpApplied();

    }

    @Override
    public void onLeftSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, 1);
    }

    @Override
    public void onRightSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, 2);
    }

    @Override
    public void onTopSwipe(Question dataObject) {
        saveSinglePrediction(dataObject, 0);
    }

    @Override
    public void onBottomSwipe(Question dataObject) {
        //dataObject.setQuestionTime(mRemainingTime);
        mPredictionAdapter.add(dataObject);
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {

        if(itemsInAdapter == 0) {
            PredictionDataHandler.getInstance().saveMyResult(mMyResult);

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.BundleKeys.TOURNAMENT_ID, mMyResult.getTournamentId());
            bundle.putString(Constants.BundleKeys.TOURNAMENT_NAME, mMyResult.getTournamentName());
            mPredictionModelListener.onSuccessCompletion(bundle);
            return;
        }

        if(itemsInAdapter == 1 && mTotalCount == 1) {
            //mPassEnabled = false;
            mPredictionModelListener.onShowingLastQuestion();
        } else if(mTotalCount == 0) {
            mPassEnabled = false;
            mTotalCount = mPredictionAdapter.getCount();
            mPredictionModelListener.onShowingPassedQuestions();
        }

        mPredictionModelListener.onQuestionChanged(mPredictionAdapter.getItem(0));
        mPredictionModelListener.getNumberofCards(itemsInAdapter);

       // mPredictionAdapter.startTimer();
    }

    @Override
    public boolean needBottomSwipe() {
        return mPassEnabled;
    }

    private void saveSinglePrediction(Question question, int answerId) {
        Answer answer = new Answer (
                ScGameDataHandler.getInstance().getUserId(),
                question.getMatchId(),
                question.getQuestionId(),
                String.valueOf(answerId),
                TimeUtils.getCurrentTime(DateFormats.FORMAT_DATE_T_TIME_ZONE, DateFormats.GMT),
                question.getPowerUpId()

        );
        PredictionDataHandler.getInstance().savePrediction(answer);
        postAnswerToServer(answer);
    }

    private void postAnswerToServer(Answer answer) {
        new PostAnswerModelImpl(new PostAnswerModelImpl.PostAnswerModelListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onNoInternet() {}

            @Override
            public void requireLogIn() {}

            @Override
            public void onFailed(String message) {}
        }).postAnswer(answer);
    }
//
//    @Override
//    public void onTimeUp() {
//        mPredictionModelListener.onTimeUp();
//    }


    public interface OnPredictionModelListener {

        Context getContext();

        void onGetAdapter(PredictionAdapter predictionAdapter,
                          SwipeFlingAdapterView.OnSwipeListener<Question> listener);

        void onSuccessCompletion(Bundle bundle);

        void onShowingLastQuestion();

        void onShowingPassedQuestions();

//        void onTimeUp();

        void dismissPowerUpApplied();

        void onNoInternet();

        void onSuccessQuestions(List<Question> questionList);

        void onFailedQuestions(String message);

        void onNoQuestions();

        void onQuestionChanged(Question item);

        void getNumberofCards(int itemsInAdapter);
    }
}