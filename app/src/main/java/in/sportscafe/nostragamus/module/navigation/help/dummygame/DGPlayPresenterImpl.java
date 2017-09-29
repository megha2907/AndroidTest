package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.navigation.help.dummygame.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.module.resultspeek.dto.Question;

/**
 * Created by Jeeva on 20/5/16.
 */
public class DGPlayPresenterImpl implements DGPlayPresenter, DGPlayModelImpl.OnDummyGamePlayModelListener {

    private DGPlayView mDummyGamePlayView;

    private DGPlayModel mDummyGamePlayModel;

    public DGPlayPresenterImpl(DGPlayView DummyGamePlayView) {
        this.mDummyGamePlayView = DummyGamePlayView;
        this.mDummyGamePlayModel = DGPlayModelImpl.newInstance(this);
    }

    public static DGPlayPresenter newInstance(DGPlayView dummyGamePlayView) {
        return new DGPlayPresenterImpl(dummyGamePlayView);
    }

    @Override
    public void onCreatePrediction(Bundle bundle) {
        mDummyGamePlayModel.init(mDummyGamePlayView.getContext(), bundle);
    }

    @Override
    public void setFlingListener(FlingCardListener topCardListener) {
        mDummyGamePlayModel.setFlingCardListener(topCardListener);
    }

    @Override
    public void updatePowerups() {
        mDummyGamePlayView.set2xPowerupCount(mDummyGamePlayModel.get2xPowerupCount(), true);
        mDummyGamePlayView.setNonegsPowerupCount(mDummyGamePlayModel.getNonegsPowerupCount(), true);
        mDummyGamePlayView.setPollPowerupCount(mDummyGamePlayModel.getPollPowerupCount(), true);
    }

    @Override
    public void onClick2xPowerup() {
        mDummyGamePlayModel.apply2xPowerup();
    }

    @Override
    public void onClickNonegsPowerup() {
        mDummyGamePlayModel.applyNonegsPowerup();
    }

    @Override
    public void onClickPollPowerup() {
        mDummyGamePlayModel.applyPollPowerup();
    }

    @Override
    public void onGetQuestions(Question question, String questionType) {
        mDummyGamePlayModel.initAdapter(mDummyGamePlayView.getContext(), question, questionType);
    }

    @Override
    public void onAdapterCreated(DGPlayAdapter predictionAdapter,
                                 SwipeFlingAdapterView.OnSwipeListener<Question> onSwipeListener) {
        mDummyGamePlayView.setAdapter(predictionAdapter, onSwipeListener);
    }

    @Override
    public void onQuestionChanged(Question question, boolean neitherAvailable) {
        if (neitherAvailable) {
            mDummyGamePlayView.showNeither();
        } else {
            mDummyGamePlayView.hideNeither();
        }

        mDummyGamePlayView.setNeitherOption(question.getQuestionOption3());
    }

    @Override
    public void notifyTopQuestion() {
        mDummyGamePlayView.notifyTopView();
    }

    @Override
    public void on2xApplied(int count, boolean reverse) {
        mDummyGamePlayView.set2xPowerupCount(count, reverse);
    }

    @Override
    public void onNonegsApplied(int count, boolean reverse) {
        mDummyGamePlayView.setNonegsPowerupCount(count, reverse);
    }

    @Override
    public void onAudiencePollApplied(int count, boolean reverse) {
        mDummyGamePlayView.setPollPowerupCount(count, reverse);
    }

    @Override
    public void onQuestionAnswered(Integer scoredPoints) {
        mDummyGamePlayView.onPlayDone(scoredPoints);
    }

    @Override
    public void onGetPowerUpDetails() {
        mDummyGamePlayView.showPowerups();
        updatePowerups();
    }

    @Override
    public void onNoPowerUps() {
    }

    @Override
    public void onRemovingPowerUps() {
        mDummyGamePlayView.onRemovingPowerUps();
    }
}