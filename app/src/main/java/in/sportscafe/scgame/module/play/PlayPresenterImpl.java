package in.sportscafe.scgame.module.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;

/**
 * Created by Jeeva on 20/5/16.
 */
public class PlayPresenterImpl implements PlayPresenter, PlayModelImpl.OnPlayModelListener {

    private PlayView mPredictionView;

    private PlayModel mPredictionModel;

    public PlayPresenterImpl(PlayView predictionView) {
        this.mPredictionView = predictionView;
        this.mPredictionModel = PlayModelImpl.newInstance(this);
    }

    public static PlayPresenter newInstance(PlayView predictionView) {
        return new PlayPresenterImpl(predictionView);
    }

    @Override
    public void onCreatePlay() {
        getAllQuestions();
    }

    private void getAllQuestions() {
        mPredictionView.showProgressbar();
        mPredictionModel.getAllQuestions();
    }

    @Override
    public void onClickSkip() {
//        mPredictionModel.skipCurrentMatch();
        populateNextMatch();
    }

    @Override
    public void onGetResultBack(int resultCode, Intent data) {
        populateNextMatch();
    }

    private void populateNextMatch() {
        if(null != mPredictionView.getContext()) {
            if (mPredictionModel.isNextContest()) {
                Bundle bundle = mPredictionModel.getNextContestQuestions();
               // mPredictionView.navigateMatchStatus(bundle);
            } else {
               //mPredictionView.navigateToAllDone();
                mPredictionView.hideSkip();
            }
        }
    }

    @Override
    public void onSuccessQuestions() {
        mPredictionView.dismissProgressbar();

        if(mPredictionModel.isNextContest()) {
            mPredictionView.showSkip();
        }

        populateNextMatch();
    }

    @Override
    public void onFailedQuestions(String message) {
        mPredictionView.dismissProgressbar();
        showAlertMessage(message);
    }

    @Override
    public void onNoQuestions() {
        mPredictionView.dismissProgressbar();
        //mPredictionView.navigateToAllDone();
    }

    @Override
    public void onNoInternet() {
        mPredictionView.dismissProgressbar();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public Context getContext() {
        return mPredictionView.getContext();
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
}