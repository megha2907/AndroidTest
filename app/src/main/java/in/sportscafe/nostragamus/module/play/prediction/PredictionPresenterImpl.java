package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
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


        mPredictionView.setContestName(mPredictionModel.getContestName(), mPredictionModel.getMatchStage());
        showPopUp();
        updatePowerups();

        if (mPredictionModel.isDummyGameShown()) {
            mPredictionModel.getAllQuestions();
        } else {
            mPredictionView.navigateToDummyGame();
        }
    }

    private void showPopUp() {

        if (NostragamusDataHandler.getInstance().getMatchPlayedCount() == 2
                && !NostragamusDataHandler.getInstance().isPowerUpApplied()
                && !NostragamusDataHandler.getInstance().isPlayedSecondMatchPopUp()) {
            mPredictionView.showPopUp(Constants.InAppPopups.SECOND_MATCH_PLAYED_WITH_NO_POWERUP);
        } else if (NostragamusDataHandler.getInstance().getMatchPlayedCount() == 4
                && !NostragamusDataHandler.getInstance().isBankInfoShown()) {
            onClickBankTransfer();
        }

//        else if (NostragamusDataHandler.newInstance().getMatchPlayedCount() == 4
//                && !NostragamusDataHandler.newInstance().isPowerUpApplied()
//                && !NostragamusDataHandler.newInstance().isPlayedFifthMatchPopUp()) {
//            mPredictionView.showPopUp(Constants.InAppPopups.FIFTH_MATCH_PLAYED_WITH_NO_POWERUP);
//        }

    }

    private void updatePowerups() {
        mPredictionView.set2xPowerupCount(mPredictionModel.get2xPowerupCount(), true);
        mPredictionView.setNonegsPowerupCount(mPredictionModel.getNonegsPowerupCount(), true);
        mPredictionView.setPollPowerupCount(mPredictionModel.getPollPowerupCount(), true);
    }

    @Override
    public void onSuccessCompletion(Bundle bundle) {

        bundle.putString(Constants.ScreenNames.PLAY,Constants.ScreenNames.PLAY);

        if (NostragamusDataHandler.getInstance().getMatchPlayedCount() == 0
                && !NostragamusDataHandler.getInstance().isPlayedFirstMatch()) {
            //mPredictionView.showFirstMatchPlayedPopUp(Constants.InAppPopups.FIRST_MATCH_PLAYED, bundle);
            bundle.putBoolean(Constants.BundleKeys.PLAYED_FIRST_MATCH,true);
            mPredictionView.navigateToResults(bundle);
        }else {
            mPredictionView.navigateToResults(bundle);
            bundle.putBoolean(Constants.BundleKeys.PLAYED_FIRST_MATCH,false);
        }
    }

    @Override
    public void onShowingLastQuestion() {
        mPredictionView.hideShuffle();
    }

    @Override
    public void onSuccessQuestions(List<Question> questions, SwipeFlingAdapterView.OnSwipeListener<Question> listener) {
        mPredictionView.showShuffle();
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
    public void onClickBack() {
        if (mPredictionModel.isAnyQuestionAnswered()) {
            mPredictionView.navigateToFeed();
        } else {
            mPredictionView.goBack();
        }
    }

    @Override
    public void onClickBankTransfer() {
        if (NostragamusDataHandler.getInstance().isBankInfoShown()) {
            mPredictionView.navigateToBankTransfer(mPredictionModel.getChallengeInfoBundle());
        } else {
            mPredictionView.showBankInfo();
        }
    }

    @Override
    public void onBankInfoDismiss() {
        NostragamusDataHandler.getInstance().setBankInfoShown(true);
        onClickBankTransfer();
    }

    @Override
    public void onChallengeInfoUpdated(Bundle bundle) {
        mPredictionModel.updateChallengeInfoValues(bundle);
        updatePowerups();
    }

    @Override
    public void onGetDummyGameResult() {
        mPredictionModel.onDummyGameShown();
        mPredictionModel.getAllQuestions();
    }

    @Override
    public void onShake() {
        if (mPredictionModel.isQuestionAvailable()) {
            mPredictionView.takeScreenshotAndShare();
            mPredictionModel.getShareText(mPredictionView.getContext());
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
    public void onAudiencePollApplied(int count, boolean reverse) {
        mPredictionView.setPollPowerupCount(count, reverse);
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

    @Override
    public void showPowerUpCountLessPopUp() {
        mPredictionView.showPopUp(Constants.InAppPopups.LESS_POWERUPS);
    }

    @Override
    public void onGetQuestionShareText(String shareText) {
        AppSnippet.copyToClipBoard(mPredictionView.getContext(), shareText);
        mPredictionView.showMessage(Alerts.DEFAULT_SHARE_MESSAGE, 15000);
    }
}