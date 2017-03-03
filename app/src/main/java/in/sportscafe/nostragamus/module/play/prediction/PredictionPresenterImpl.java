package in.sportscafe.nostragamus.module.play.prediction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PredictionPresenterImpl implements PredictionPresenter, PredictionModelImpl.OnPredictionModelListener {

    private PredictionView mPredictionView;

    private PredictionModel mPredictionModel;

    public PredictionPresenterImpl(PredictionView predictionView) {
        this.mPredictionView = predictionView;
        this.mPredictionModel = PredictionModelImpl.newInstance(this);
    }

    public static PredictionPresenter newInstance(PredictionView predictionView) {
        return new PredictionPresenterImpl(predictionView);
    }

    @Override
    public void onCreatePrediction(Bundle bundle) {
        mPredictionModel.init(bundle);

        if (!mPredictionModel.isDummyGame()) {
            mPredictionModel.getAllQuestions();

            mPredictionView.setContestName(mPredictionModel.getContestName());

            checkShowStatusOfBankInfo();
        } else {
            mPredictionView.changeToDummyGameMode();
        }

        updatePowerups();
    }

    private void checkShowStatusOfBankInfo() {
        if (NostragamusDataHandler.getInstance().isBankInfoFirstTimeChecked()
                && !NostragamusDataHandler.getInstance().isBankInfoShown()) {
            mPredictionView.showBankInfo(null);
        }
    }

    private void updatePowerups() {
        mPredictionView.set2xPowerupCount(mPredictionModel.get2xPowerupCount(), true);
        mPredictionView.setNonegsPowerupCount(mPredictionModel.getNonegsPowerupCount(), true);
        mPredictionView.setPollPowerupCount(mPredictionModel.getPollPowerupCount(), true);
    }

    @Override
    public void onSuccessCompletion(Bundle bundle) {
        mPredictionView.navigateToFeed();
    }

    @Override
    public void onShowingLastQuestion() {
        mPredictionView.hideShuffle();
    }

    @Override
    public void onSuccessQuestions(List<Question> questions, SwipeFlingAdapterView.OnSwipeListener<Question> listener) {
        mPredictionView.setAdapter(mPredictionModel.getAdapter(mPredictionView.getContext(), questions), listener);
    }

    @Override
    public void setFlingListener(FlingCardListener topCardListener) {
        mPredictionModel.setFlingCardListener(topCardListener);
    }

    @Override
    public void onClick2xPowerup() {
        mPredictionModel.apply2xPowerup();
    }

    @Override
    public void onClickNonegsPowerup() {
        mPredictionModel.applyNonegsPowerup();
    }

    @Override
    public void onClickPollPowerup() {
        mPredictionModel.applyPollPowerup();
    }

    @Override
    public void onDummyGameStart() {
        mPredictionModel.getDummyGameQuestions();
    }

    @Override
    public void onDummyGameEnd() {
        checkWhereToGo();
    }

    @Override
    public void onClickBack() {
        if (!mPredictionModel.isDummyGame()) {
            if (mPredictionModel.isAnyQuestionAnswered()) {
                mPredictionView.navigateToFeed();
            } else {
                mPredictionView.goBack();
            }
        } else if (!mPredictionView.dismissCoach()) {
            checkWhereToGo();
        }
    }

    @Override
    public void onClickSkip() {
        mPredictionModel.onSkippingDummyGame();
        onClickBack();
    }

    @Override
    public void onClickBankTransfer() {
        if (NostragamusDataHandler.getInstance().isBankInfoShown()) {
            mPredictionView.navigateToBankTransfer(mPredictionModel.getChallengeInfoBundle());
        } else {
            mPredictionView.showBankInfo(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    NostragamusDataHandler.getInstance().setBankInfoShown(true);
                    PredictionPresenterImpl.this.onClickBankTransfer();
                }
            });
        }
    }

    @Override
    public void onPowerUpUpdated(Bundle bundle) {
        mPredictionModel.updatePowerUpValues(bundle);
        updatePowerups();
    }

    private void checkWhereToGo() {
        if (mPredictionModel.isFromSettings()) {
            mPredictionView.goBack();
        } else {
            mPredictionView.navigateToHome();
        }
    }

    @Override
    public void onFailedQuestions(String message) {
        showAlertMessage(message);
    }

    @Override
    public void onNoQuestions() {
        showAlertMessage(Alerts.NO_QUESTIONS);
    }

    @Override
    public void onQuestionChanged(Question question, int initialCount, boolean neitherAvailable) {
        mPredictionView.setNumberofCards(question.getQuestionNumber() + "/" + initialCount);

        if (neitherAvailable) {
            mPredictionView.showNeither();
        } else {
            mPredictionView.hideNeither();
        }
        mPredictionView.setNeitherOption(question.getQuestionOption3());

        if (mPredictionModel.isDummyGame()) {
            handleDummyGameQuestionChange(question.getQuestionNumber());
        }
    }

    private void handleDummyGameQuestionChange(int questionNumber) {
        if (questionNumber == 1) {
            mPredictionView.hideNeither();
            mPredictionView.hidePowerups();

            mPredictionView.showLeftRightCoach();
            mPredictionView.showLeftRightIndicator();
        } else if (questionNumber == 2) {
            mPredictionView.showNeither();
            mPredictionView.disableLeftRightOptions();

            mPredictionView.showNeitherIndicator();
            mPredictionView.showNeitherCoach();
        } else if (questionNumber == 3) {
            mPredictionView.enableLeftRightOptions();
            mPredictionView.showPowerups();

            mPredictionView.showPowerupsHint();
            mPredictionView.showPowerupsCoach();
        } else if (questionNumber == 4) {
            mPredictionView.hidePowerupsHint();
        }
    }

    @Override
    public void onFailedAudiencePollResponse() {
        mPredictionView.showMessage(Alerts.AUDIENCE_POLL_FAIL);
    }

    @Override
    public void notifyTopQuestion() {
        mPredictionView.notifyTopView();
    }

    @Override
    public void onGetSport(Integer sportId) {
        mPredictionView.changeBackgroundImage(sportId);
    }

    @Override
    public void on2xApplied(int count, boolean reverse) {
        mPredictionView.set2xPowerupCount(count, reverse);
    }

    @Override
    public void onNonegsApplied(int count, boolean reverse) {
        mPredictionView.setNonegsPowerupCount(count, reverse);
    }

    @Override
    public void onAudiencePollApplied(int count) {
        mPredictionView.setPollPowerupCount(count, false);
    }

    @Override
    public void onNoInternet() {
        mPredictionView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onNoInternetForQuestions() {
        showAlertMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    private void showAlertMessage(String message) {
        mPredictionView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPredictionModel.getAllQuestions();
                    }
                });
    }

    @Override
    public void onPostAnswerFailed(String message) {
        mPredictionView.showMessage(Alerts.API_FAIL);
        onClickBack();
    }

    @Override
    public void onMatchAlreadyStarted() {
        mPredictionView.showMessage(Alerts.MATCH_ALREADY_STARTED);
        onClickBack();
    }

    @Override
    public void noInternetOnPostingAnswer() {
        mPredictionView.showMessage(Alerts.NO_NETWORK_CONNECTION);
        onClickBack();
    }

    @Override
    public void onDummyGameCompletion() {
        mPredictionView.showDummyGameInfo();
    }

    @Override
    public void onApiCallStarted() {
        mPredictionView.showProgressbar();
    }

    @Override
    public boolean onApiCallStopped() {
        return mPredictionView.dismissProgressbar();
    }

    @Override
    public void onNoPowerUps() {
        onClickBankTransfer();
    }
}