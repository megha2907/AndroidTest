package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PredictionPresenterImpl implements PredictionPresenter, PredictionModelImpl.OnPredictionModelListener {

    private PredictionView mPredictionView;

    private FlingCardListener mFlingCardListener;

    private PredictionModel mPredictionModel;

    public PredictionPresenterImpl(PredictionView predictionView) {
        this.mPredictionView = predictionView;
        this.mFlingCardListener = mFlingCardListener;
        this.mPredictionModel = PredictionModelImpl.newInstance(this);
    }

    public static PredictionPresenter newInstance(PredictionView predictionView) {
        return new PredictionPresenterImpl(predictionView);
    }

    @Override
    public void onCreatePrediction(Bundle bundle) {
        mPredictionModel.saveData(bundle);
        if(!mPredictionModel.isDummyGame()) {
            mPredictionView.setTournamentName(mPredictionModel.getTournamentName());
            mPredictionView.setContestName(mPredictionModel.getContestName());
            mPredictionView.setNumberofPowerups(NostragamusDataHandler.getInstance().getNumberof2xPowerups(),
                    NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups(),
                    NostragamusDataHandler.getInstance().getNumberofNonegsPowerups());
            mPredictionView.setTournamentPhoto(mPredictionModel.getTournamentPhoto());
        }
    }

    @Override
    public Context getContext() {
        return mPredictionView.getContext();
    }

    @Override
    public void onGetAdapter(PredictionAdapter predictionAdapter,
                             SwipeFlingAdapterView.OnSwipeListener<Question> listener) {
        mPredictionView.setAdapter(predictionAdapter, listener);
    }

    @Override
    public void onSuccessCompletion(Bundle bundle) {
        mPredictionView.navigateToResult(bundle);
    }

    private void getAllQuestions() {
        mPredictionView.showProgressbar();
        mPredictionModel.getAllQuestions();
    }

    @Override
    public void onShowingLastQuestion() {
        mPredictionView.hideShuffle();
        //mPredictionView.showLastQuestionAlert();
    }

    @Override
    public void onShowingPassedQuestions() {
        mPredictionView.hideShuffle();
        //mPredictionView.showNoNegativeAlert();
        //mPredictionView.showMessage(Constants.Alerts.PASSED_QUESTION_ALERT);
    }


    public void onPowerUp(String powerup) {
        mPredictionModel.updatePowerUps(powerup);
    }

    @Override
    public void onSuccessQuestions() {
        mPredictionView.dismissProgressbar();
    }

    @Override
    public void setFlingListener(FlingCardListener topCardListener) {
        mPredictionModel.setFlingCardListener(topCardListener);
    }


    @Override
    public void onFailedQuestions(String message) {
        mPredictionView.dismissProgressbar();
        showAlertMessage(message);
    }

    @Override
    public void onNoQuestions() {
        mPredictionView.dismissProgressbar();
        showAlertMessage(Constants.Alerts.NO_QUESTIONS);
    }

    @Override
    public void onQuestionChanged(Question question, int minitialCount, boolean neitherAvailable) {
        mPredictionView.setNumberofCards(question.getQuestionNumber(),minitialCount);
        mPredictionView.setNeitherOption(question.getQuestionOption3());
        if(neitherAvailable) {
            mPredictionView.showNeither();
        } else {
            mPredictionView.hideNeither();
        }
    }

    @Override
    public void onFailedAudiencePollResponse(String message) {
        mPredictionView.dismissProgressbar();
        mPredictionView.dismissPowerUp();
        mPredictionView.showMessage(Constants.Alerts.AUDIENCE_POLL_FAIL);
    }

    @Override
    public void onSuccessAudiencePollResponse() {
        mPredictionView.dismissProgressbar();
        mPredictionView.updateAudiencePollPowerup();
    }

    @Override
    public void onNegativePowerUpApplied() {
        mPredictionView.notifyTopView();
    }

    @Override
    public void onGetSportName(String sportName) {
        mPredictionView.changeBackgroundImage(sportName);
    }

    @Override
    public void onFailedPostAnswerToServer(String message) {

        if (message.equalsIgnoreCase("Match has already started")){
            mPredictionView.showMessage(Constants.Alerts.MATCH_ALREADY_STARTED);
        }else {
            mPredictionView.showMessage(Constants.Alerts.API_FAIL);
        }
        mPredictionView.goBack();
    }

    @Override
    public View getRootView() {
        return mPredictionView.getRootView();
    }

    @Override
    public void onNoInternet() {
        mPredictionView.dismissProgressbar();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
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
    public void dismissPowerUpApplied() {
        mPredictionView.dismissPowerUp();
    }
}