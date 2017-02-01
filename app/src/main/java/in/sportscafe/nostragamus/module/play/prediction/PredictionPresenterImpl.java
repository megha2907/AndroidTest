package in.sportscafe.nostragamus.module.play.prediction;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

import static com.google.android.gms.analytics.internal.zzy.m;

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
            getAllQuestions();

            mPredictionView.setTournamentPhoto(mPredictionModel.getTournamentPhoto());
            mPredictionView.setTournamentName(mPredictionModel.getTournamentName());
            mPredictionView.setContestName(mPredictionModel.getContestName());

        } else {
            mPredictionView.changeToDummyGameMode();
        }

        updatePowerups();
    }

    private void updatePowerups() {
        mPredictionView.set2xPowerupCount(mPredictionModel.get2xPowerupCount());
        mPredictionView.setNonegsPowerupCount(mPredictionModel.getNonegsPowerupCount());
        mPredictionView.setPollPowerupCount(mPredictionModel.getPollPowerupCount());
    }

    @Override
    public void onSuccessCompletion(Bundle bundle) {
        mPredictionView.dismissProgressbar();
        mPredictionView.navigateToFeed(bundle);
    }

    private void getAllQuestions() {
        mPredictionView.showProgressbar();
        mPredictionModel.getAllQuestions();
    }

    @Override
    public void onShowingLastQuestion() {
        mPredictionView.hideShuffle();
    }

    @Override
    public void onSuccessQuestions(List<Question> questions, SwipeFlingAdapterView.OnSwipeListener<Question> listener) {
        mPredictionView.dismissProgressbar();
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
        mPredictionView.navigateToHome();
    }

    @Override
    public void onFailedQuestions(String message) {
        mPredictionView.dismissProgressbar();
        showAlertMessage(message);
    }

    @Override
    public void onNoQuestions() {
        mPredictionView.dismissProgressbar();
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

        if(mPredictionModel.isDummyGame()) {
            handleDummyGameQuestionChange(question);
        }
    }

    private void handleDummyGameQuestionChange(Question question) {
        int questionNumber = question.getQuestionNumber();
        if(questionNumber == 1) {
            mPredictionView.hideNeither();
            mPredictionView.hidePowerups();
        } else if(questionNumber == 2) {
            mPredictionView.showNeither();
            mPredictionView.disableLeftRightOptions();
        } else if(questionNumber == 3) {
            mPredictionView.enableLeftRightOptions();
            mPredictionView.showPowerups();
            mPredictionView.showPowerupsHint();
        } else if(questionNumber == 4) {
            mPredictionView.hidePowerupsHint();
        }
    }

    @Override
    public void onSuccessAudiencePollResponse() {
        mPredictionView.dismissProgressbar();
    }

    @Override
    public void onFailedAudiencePollResponse() {
        mPredictionView.dismissProgressbar();
        mPredictionView.showMessage(Alerts.AUDIENCE_POLL_FAIL);
    }

    @Override
    public void notifyTopQuestion() {
        mPredictionView.notifyTopView();
    }

    @Override
    public void onGetSportName(String sportName) {
        mPredictionView.changeBackgroundImage(sportName);
    }

    @Override
    public void onFailedPostAnswerToServer(String message) {
        if (message.equalsIgnoreCase("Match has already started")) {
            mPredictionView.showMessage(Alerts.MATCH_ALREADY_STARTED);
        } else {
            mPredictionView.showMessage(Alerts.API_FAIL);
        }
        mPredictionView.goBack();
    }

    @Override
    public void on2xApplied(int count) {
        mPredictionView.set2xPowerupCount(count);
    }

    @Override
    public void onNonegsApplied(int count) {
        mPredictionView.setNonegsPowerupCount(count);
    }

    @Override
    public void onAudiencePollApplied(int count) {
        mPredictionView.setPollPowerupCount(count);
    }

    @Override
    public void onDummyGameCompletion() {
        mPredictionView.showDummyGameInfo();
    }

    @Override
    public void onNoInternet() {
        mPredictionView.dismissProgressbar();
        mPredictionView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onNoInternetForQuestions() {
        mPredictionView.dismissProgressbar();
        showAlertMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    private void showAlertMessage(String message) {
        mPredictionView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getAllQuestions();
                    }
                });
    }

    @Override
    public boolean isThreadAlive() {
        return null != mPredictionView.getContext();
    }
}