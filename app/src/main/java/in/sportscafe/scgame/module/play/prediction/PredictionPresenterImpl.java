package in.sportscafe.scgame.module.play.prediction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;

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
        mPredictionModel.saveData(bundle);
        mPredictionView.setTournamentName(mPredictionModel.getTournamentName());
        mPredictionView.setContestName(mPredictionModel.getContestName());
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
        mPredictionView.hidePass();
        mPredictionView.showLastQuestionAlert();
    }

    @Override
    public void onShowingPassedQuestions() {
        mPredictionView.hidePass();
        mPredictionView.showNoNegativeAlert();
        mPredictionView.showMessage(Constants.Alerts.PASSED_QUESTION_ALERT);
    }


    public void onPowerUp() {
        mPredictionModel.updatePowerUps();
    }

    @Override
    public void onSuccessQuestions() {
        mPredictionView.dismissProgressbar();
        //populateNextMatch();
    }

    @Override
    public void onFailedQuestions(String message) {
        mPredictionView.dismissProgressbar();
        showAlertMessage(message);
    }

    @Override
    public void onNoQuestions() {
        mPredictionView.dismissProgressbar();
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