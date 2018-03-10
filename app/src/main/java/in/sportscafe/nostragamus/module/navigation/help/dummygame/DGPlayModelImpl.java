package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants.AnswerIds;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.module.resultspeek.dto.AudiencePoll;
import in.sportscafe.nostragamus.module.resultspeek.dto.AudiencePollResponse;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;

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
    }

    public static DGPlayModel newInstance(OnDummyGamePlayModelListener predictionModelListener) {
        return new DGPlayModelImpl(predictionModelListener);
    }

    @Override
    public void init(Context context, Bundle bundle) {
        if (null != bundle && bundle.containsKey(BundleKeys.DUMMY_QUESTION)) {
            Question dummyQuestion = Parcels.unwrap(bundle.getParcelable(BundleKeys.DUMMY_QUESTION));
            initAdapter(context, dummyQuestion, bundle.getString(BundleKeys.DUMMY_QUESTION_TYPE));
        } else {
            mModelListener.onGetPowerUpDetails();
        }
    }

    @Override
    public void initAdapter(Context context, Question question, String questionType) {
        if (context != null) {
            mPredictionAdapter = new DGPlayAdapter(context, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAppliedPowerUp(view);
                    changePowerupInstructionBasedUponRemoval();

                }
            }, questionType);

            mPredictionAdapter.add(question);
            mModelListener.onAdapterCreated(mPredictionAdapter, this);
        }
    }

    private void changePowerupInstructionBasedUponRemoval() {
        if (mPredictionAdapter != null && mPredictionAdapter.getTopQuestion() != null) {
            ArrayList<String> powerupArray = mPredictionAdapter.getTopQuestion().getPowerUpArrayList();
            if (powerupArray == null || powerupArray.size() == 0 ||
                    (powerupArray.size() == 1 && powerupArray.get(0).equals(Powerups.AUDIENCE_POLL))) {
                mModelListener.onRemovingPowerUps();
            }
        }
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
        if (m2xPowerups > 0 && mPredictionAdapter != null) {
            boolean isApplied = mPredictionAdapter.getTopQuestion().apply2xPowerUp();
            if (isApplied) {
                mPredictionAdapter.add2xPowerup();
                mModelListener.notifyTopQuestion();

                m2xPowerups--;
                mModelListener.on2xApplied(m2xPowerups, false);
            }
        } else {
            mModelListener.onNoPowerUps();
        }
    }

    @Override
    public void applyNonegsPowerup() {
        if (mNonegsPowerups > 0 && mPredictionAdapter != null) {
            boolean isApplied = mPredictionAdapter.getTopQuestion().applyNonegsPowerUp();
            if (isApplied) {
                mPredictionAdapter.addNoNegativePowerup();
                mModelListener.notifyTopQuestion();

                mNonegsPowerups--;
                mModelListener.onNonegsApplied(mNonegsPowerups, false);
            }
        } else {
            mModelListener.onNoPowerUps();
        }
    }

    @Override
    public void applyPollPowerup() {
        if (mPollPowerups > 0) {
            handleAudiencePollResponse(getDummyGameAudiencePoll().getAudiencePoll());
        } else {
            mModelListener.onNoPowerUps();
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
            mLastAnsweredQuestion.removeAppliedPowerUp(Powerups.XX);
            mLastAnsweredQuestion.removeAppliedPowerUp(Powerups.NO_NEGATIVE);
            mLastAnsweredQuestion.removePollPowerUp();

            mModelListener.onQuestionAnswered(scoredPoints);
            return;
        }

        Question topQuestion = mPredictionAdapter.getTopQuestion();
        if (mLastQuestionNumber != topQuestion.getQuestionNumber()) {
            mLastQuestionNumber = topQuestion.getQuestionNumber();

            mNeitherOptionAvailable = !TextUtils.isEmpty(topQuestion.getQuestionOption3());
            ArrayList<String> powerupArr = topQuestion.getPowerUpArrayList();
            if (powerupArr != null) {
                for (String powerupStr : powerupArr) {
                    updatePowerUpStatus(powerupStr);
                }
            }
            mModelListener.onQuestionChanged(topQuestion, mNeitherOptionAvailable);
        }
    }

    @Override
    public boolean needLeftSwipe() {
        return true; //!mNeitherOptionAvailable;
    }

    @Override
    public boolean needRightSwipe() {
        return true; //!mNeitherOptionAvailable;
    }

    @Override
    public boolean needTopSwipe() {
        return true; // mNeitherOptionAvailable;
    }

    @Override
    public boolean needBottomSwipe() {
        return false;
    }

    @Override
    public void onCardMoving(float xPercent, float yPercent) {
        mPredictionAdapter.onCardMoving(xPercent, yPercent);
    }

    private void handleAudiencePollResponse(List<AudiencePoll> audiencePoll) {
        int leftAnswerPercent = Integer.parseInt(audiencePoll.get(0).getAnswerPercentage().replaceAll("%", ""));
        int rightAnswerPercent = Integer.parseInt(audiencePoll.get(1).getAnswerPercentage().replaceAll("%", ""));

        if (mPredictionAdapter != null && mPredictionAdapter.getTopQuestion() != null) {
            boolean isApplied = mPredictionAdapter.getTopQuestion().applyAudiencePollPowerUp(leftAnswerPercent, rightAnswerPercent);
            if (isApplied) {
                mPredictionAdapter.addAudiencePoll();
                mModelListener.notifyTopQuestion();

                mPollPowerups--;
                mModelListener.onAudiencePollApplied(mPollPowerups, false);
            }
        }
    }

    private void removeAppliedPowerUp(View view) {
        if (view != null) {
            String powerUpOfThisView = (String) view.getTag();
            if (!TextUtils.isEmpty(powerUpOfThisView)) {

                Question topQuestion = mPredictionAdapter.getTopQuestion();
                increasePowerUpCount(powerUpOfThisView);
                topQuestion.removeAppliedPowerUp(powerUpOfThisView);

                if (powerUpOfThisView.equals(Powerups.XX)) {
                    mPredictionAdapter.remove2xPowerup(view);
                } else if (powerUpOfThisView.equals(Powerups.NO_NEGATIVE)) {
                    mPredictionAdapter.removeNoNegativePowerup(view);
                }
                mModelListener.notifyTopQuestion();
            }
        }
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
        switch (powerUpId) {
            case Powerups.XX:
                mModelListener.on2xApplied(m2xPowerups, !(Powerups.XX == powerUpId));
                break;
            case Powerups.NO_NEGATIVE:
                mModelListener.onNonegsApplied(mNonegsPowerups, !(Powerups.NO_NEGATIVE == powerUpId));
                break;
            case Powerups.AUDIENCE_POLL:
                mModelListener.onAudiencePollApplied(mPollPowerups, !(Powerups.AUDIENCE_POLL == powerUpId));
                break;
        }
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

        void onRemovingPowerUps();
    }
}