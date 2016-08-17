package in.sportscafe.scgame.module.play.prediction;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.offline.PredictionDataHandler;
import in.sportscafe.scgame.module.play.prediction.dto.Answer;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.scgame.utils.timeutils.TimeUtils;

import static in.sportscafe.scgame.Constants.BundleKeys;
import static in.sportscafe.scgame.Constants.DateFormats;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PredictionModelImpl implements PredictionModel,
        SwipeFlingAdapterView.OnSwipeListener<Question>,PredictionAdapter.OnPredictionTimerListener {

    private PredictionAdapter mPredictionAdapter;

    private OnPredictionModelListener mPredictionModelListener;

    private Match mMyResult;

    private int mTotalCount;

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
        mMyResult = (Match) bundle.getSerializable(BundleKeys.CONTEST_QUESTIONS);

        populateAdapterData(mMyResult.getQuestions());
    }

    private void populateAdapterData(List<Question> questionList) {
        mTotalCount = questionList.size();

        mPredictionAdapter = new PredictionAdapter(mPredictionModelListener.getContext(),
                this, mTotalCount);
        mPredictionModelListener.onGetAdapter(mPredictionAdapter, this);

        int count = 1;
        for (Question question : questionList) {
            question.setQuestionNumber(count++);
            mPredictionAdapter.add(question);
        }

        mPredictionAdapter.notifyDataSetChanged();
    }

    @Override
    public String getTournamentName() {
        return mMyResult.getTournamentName();
    }

    @Override
    public String getContestName() {
        return mMyResult.getParties();
    }

    @Override
    public void removeFirstObjectInAdapter(Question dataObject) {
        mRemainingTime = mPredictionAdapter.getRemainingTime();
        Log.d("Remaining Time", mRemainingTime + " seconds");

        mTotalCount--;
        mPredictionAdapter.stopTimer();
        mPredictionAdapter.remove(dataObject);
        mPredictionAdapter.notifyDataSetChanged();
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
        dataObject.setQuestionTime(mRemainingTime);
        mPredictionAdapter.add(dataObject);
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if(itemsInAdapter == 0) {
            PredictionDataHandler.getInstance().saveMyResult(mMyResult);

            Bundle bundle = new Bundle();
            mPredictionModelListener.onSuccessCompletion(bundle);
            return;
        }

        if(itemsInAdapter == 1 && mTotalCount == 1) {
            mPassEnabled = false;
            mPredictionModelListener.onShowingLastQuestion();
        } else if(mTotalCount == 0) {
            mPassEnabled = false;
            mTotalCount = mPredictionAdapter.getCount();
            mPredictionModelListener.onShowingPassedQuestions();
        }

        mPredictionAdapter.startTimer();
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

    @Override
    public void onTimeUp() {
        mPredictionModelListener.onTimeUp();
    }

    public interface OnPredictionModelListener {

        Context getContext();

        void onGetAdapter(PredictionAdapter predictionAdapter,
                          SwipeFlingAdapterView.OnSwipeListener<Question> listener);

        void onSuccessCompletion(Bundle bundle);

        void onShowingLastQuestion();

        void onShowingPassedQuestions();

        void onTimeUp();
    }
}