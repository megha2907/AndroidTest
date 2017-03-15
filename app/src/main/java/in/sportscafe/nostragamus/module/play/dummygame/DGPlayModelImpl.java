package in.sportscafe.nostragamus.module.play.dummygame;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;

import com.jeeva.android.ExceptionTracker;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnswerIds;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePoll;
import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public class DGPlayModelImpl implements DGPlayModel, SwipeFlingAdapterView.OnSwipeListener<Question> {

    private DGPlayAdapter mPredictionAdapter;

    private OnDummyGamePlayModelListener mModelListener;

    private boolean mNeitherOptionAvailable = false;

    private int m2xPowerups = 2;

    private int mNonegsPowerups = 2;

    private int mPollPowerups = 2;

    private Question mLastAnsweredQuestion = null;

    private int mLastQuestionNumber = -1;

    public DGPlayModelImpl(OnDummyGamePlayModelListener predictionModelListener) {
        this.mModelListener = predictionModelListener;

        NostragamusAnalytics.getInstance().trackDummyGame(AnalyticsActions.STARTED);
    }

    public static DGPlayModel newInstance(OnDummyGamePlayModelListener predictionModelListener) {
        return new DGPlayModelImpl(predictionModelListener);
    }

    @Override
    public void init(Context context, Bundle bundle) {
        if (null != bundle && bundle.containsKey(BundleKeys.DUMMY_QUESTION)) {
            Question dummyQuestion = Parcels.unwrap(bundle.getParcelable(BundleKeys.DUMMY_QUESTION));
            initAdapter(context, dummyQuestion);
        } else {
            mModelListener.onGetPowerUpDetails();
        }
    }

    @Override
    public void initAdapter(Context context, Question question) {
        mPredictionAdapter = new DGPlayAdapter(context, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPowerUpAnimation(view);
            }
        });
        mPredictionAdapter.add(question);
        mModelListener.onAdapterCreated(mPredictionAdapter, this);
    }

    @Override
    public int get2xPowerupCount() {
        return m2xPowerups;
    }

    @Override
    public int getNonegsPowerupCount() {
        return mNonegsPowerups;
    }

    @Override
    public int getPollPowerupCount() {
        return mPollPowerups;
    }

    @Override
    public void setFlingCardListener(FlingCardListener flingCardListener) {
        mPredictionAdapter.setFlingCardListener(flingCardListener);
    }

    @Override
    public void apply2xPowerup() {
        if (isNotPowerupApplied()) {
            if (m2xPowerups > 0) {
                mPredictionAdapter.getTopQuestion().apply2xPowerUp();
                notifyTopQuestion();

                m2xPowerups--;
                mModelListener.on2xApplied(m2xPowerups, false);
            } else {
                mModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public void applyNonegsPowerup() {
        if (isNotPowerupApplied()) {
            if (mNonegsPowerups > 0) {
                mPredictionAdapter.getTopQuestion().applyNonegsPowerUp();
                notifyTopQuestion();

                mNonegsPowerups--;
                mModelListener.onNonegsApplied(mNonegsPowerups, false);
            } else {
                mModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public void applyPollPowerup() {
        if (isNotPowerupApplied()) {
            if (mPollPowerups > 0) {
                handleAudiencePollResponse(getDummyGameAudiencePoll().getAudiencePoll());
            } else {
                mModelListener.onNoPowerUps();
            }
        }
    }

    @Override
    public void removeFirstObjectInAdapter(Question question) {
        mPredictionAdapter.remove(question);
        mPredictionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLeftSwipe(Question question) {
        question.setAnswerId(AnswerIds.LEFT);
        mLastAnsweredQuestion = question;
    }

    @Override
    public void onRightSwipe(Question question) {
        question.setAnswerId(AnswerIds.RIGHT);
        mLastAnsweredQuestion = question;
    }

    @Override
    public void onTopSwipe(Question question) {
        question.setAnswerId(AnswerIds.NEITHER);
        mLastAnsweredQuestion = question;
    }

    @Override
    public void onBottomSwipe(Question question) {
        mPredictionAdapter.add(question);
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 0) {
            Integer scoredPoints = null;
            if (null != mLastAnsweredQuestion && null != mLastAnsweredQuestion.getQuestionAnswer()) {
                if (mLastAnsweredQuestion.getAnswerId() == mLastAnsweredQuestion.getQuestionAnswer()) {
                    scoredPoints = mLastAnsweredQuestion.getUpdatedPositivePoints();
                } else {
                    scoredPoints = mLastAnsweredQuestion.getUpdatedNegativePoints();
                }
            }

            mLastAnsweredQuestion.setAnswerId(null);
            mLastAnsweredQuestion.removeAppliedPowerUp();
            mLastAnsweredQuestion.removePollPowerUp();

            mModelListener.onQuestionAnswered(scoredPoints);
            return;
        }

        Question topQuestion = mPredictionAdapter.getTopQuestion();
        if (mLastQuestionNumber != topQuestion.getQuestionNumber()) {
            mLastQuestionNumber = topQuestion.getQuestionNumber();

            mNeitherOptionAvailable = !TextUtils.isEmpty(topQuestion.getQuestionOption3());
            updatePowerUpStatus(topQuestion.getPowerUpId());
            mModelListener.onQuestionChanged(topQuestion, mNeitherOptionAvailable);
        }
    }

    @Override
    public boolean needLeftSwipe() {
        return true;
    }

    @Override
    public boolean needRightSwipe() {
        return true;
    }

    @Override
    public boolean needTopSwipe() {
        return mNeitherOptionAvailable;
    }

    @Override
    public boolean needBottomSwipe() {
        return false;
    }

    @Override
    public void onCardMoving(float xPercent, float yPercent) {
        mPredictionAdapter.onCardMoving(xPercent, yPercent);
    }

    private boolean isNotPowerupApplied() {
        try {
            return null == mPredictionAdapter.getTopQuestion().getPowerUpId();
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return false;
    }

    private void notifyTopQuestion() {
        mPredictionAdapter.notifyDataSetChanged();
        mModelListener.notifyTopQuestion();
    }

    private void handleAudiencePollResponse(List<AudiencePoll> audiencePoll) {
        int leftAnswerPercent = Integer.parseInt(audiencePoll.get(0).getAnswerPercentage().replaceAll("%", ""));
        int rightAnswerPercent = Integer.parseInt(audiencePoll.get(1).getAnswerPercentage().replaceAll("%", ""));

        mPredictionAdapter.getTopQuestion().applyAudiencePollPowerUp(leftAnswerPercent, rightAnswerPercent);
        notifyTopQuestion();

        mPollPowerups--;
        mModelListener.onAudiencePollApplied(mPollPowerups, false);
    }

    private void dismissPowerUpAnimation(final View view) {
        mPredictionAdapter.dismissPowerUpAnimation(view, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                removeAppliedPowerUp();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void removeAppliedPowerUp() {
        Question topQuestion = mPredictionAdapter.getTopQuestion();
        increasePowerUpCount(topQuestion.getPowerUpId());
        topQuestion.removeAppliedPowerUp();

        notifyTopQuestion();
    }

    private void increasePowerUpCount(String powerUpId) {
        switch (powerUpId) {
            case Powerups.XX:
                mModelListener.on2xApplied(++m2xPowerups, true);
                break;
            case Powerups.NO_NEGATIVE:
                mModelListener.onNonegsApplied(++mNonegsPowerups, true);
                break;
        }
    }

    private void updatePowerUpStatus(String powerUpId) {
        mModelListener.on2xApplied(m2xPowerups, !(Powerups.XX == powerUpId));
        mModelListener.onNonegsApplied(mNonegsPowerups, !(Powerups.NO_NEGATIVE == powerUpId));
        mModelListener.onAudiencePollApplied(mPollPowerups, !(Powerups.AUDIENCE_POLL == powerUpId));
    }

    private AudiencePollResponse getDummyGameAudiencePoll() {
        AudiencePollResponse response = new AudiencePollResponse();

        List<AudiencePoll> audiencePollList = new ArrayList<>();
        response.setAudiencePoll(audiencePollList);

        AudiencePoll audiencePoll = new AudiencePoll();
        audiencePoll.setAnswerId(AnswerIds.LEFT);
        audiencePoll.setAnswerPercentage("30%");
        audiencePollList.add(audiencePoll);

        audiencePoll = new AudiencePoll();
        audiencePoll.setAnswerId(AnswerIds.RIGHT);
        audiencePoll.setAnswerPercentage("70%");
        audiencePollList.add(audiencePoll);

        return response;
    }

    public interface OnDummyGamePlayModelListener {

        void onAdapterCreated(DGPlayAdapter predictionAdapter,
                              SwipeFlingAdapterView.OnSwipeListener<Question> onSwipeListener);

        void onQuestionChanged(Question question, boolean neitherAvailable);

        void notifyTopQuestion();

        void on2xApplied(int count, boolean reverse);

        void onNonegsApplied(int count, boolean reverse);

        void onAudiencePollApplied(int count, boolean reverse);

        void onQuestionAnswered(Integer scoredPoints);

        void onGetPowerUpDetails();

        void onNoPowerUps();
    }
}