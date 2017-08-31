package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultPresenterImpl implements MyResultsPresenter, MyResultsModelImpl.OnMyResultsModelListener {

    private MyResultsView mResultsView;

    private MyResultsModel mResultsModel;

    private MyResultPresenterImpl(MyResultsView resultsView) {
        this.mResultsView = resultsView;
        this.mResultsModel = MyResultsModelImpl.newInstance(this);
    }

    public static MyResultPresenterImpl newInstance(MyResultsView resultsView) {
        return new MyResultPresenterImpl(resultsView);
    }

    @Override
    public void onCreateMyResults(Bundle bundle) {
        mResultsModel.init(bundle);
        getResultDetails();
        mResultsView.setNumberofPowerups(NostragamusDataHandler.getInstance().getReplayPowerupsCount(),
                NostragamusDataHandler.getInstance().getFlipPowerupsCount());
    }

    @Override
    public void onPowerUp(String powerup) {
        if (powerup.equalsIgnoreCase(Powerups.MATCH_REPLAY)) {
            mResultsView.openReplayDialog();
        } else if (powerup.equalsIgnoreCase(Powerups.ANSWER_FLIP)) {
            mResultsView.openFlipDialog();
        }
    }

    @Override
    public void onsetMatchDetails(Match match) {
        mResultsView.setMatchDetails(match);
//        mResultsView.navigatetoPlay(Match);
    }

    @Override
    public void onGetShareText(String shareText) {
        mResultsView.dismissProgressbar();

        AppSnippet.copyToClipBoard(mResultsView.getContext(), shareText);
        mResultsView.showMessage(Alerts.DEFAULT_SHARE_MESSAGE, 15000);
    }

    @Override
    public void onGetShareTextFailed() {
        mResultsView.dismissProgressbar();
        mResultsView.showMessage("Sharing failed, Try again", Toast.LENGTH_LONG);
    }

    @Override
    public void setToolbarHeading(String result) {
        mResultsView.setToolbarHeading(result);
    }

    @Override
    public void showResultsToBeDeclared(boolean playedFirstMatch, Match match) {
        mResultsView.showResultsToBeDeclaredView(playedFirstMatch, match);
    }

    @Override
    public void onSuccessChangeAnswerResponse(Match match) {
        //mResultsView.updateAnswers(Match);
        mResultsView.dismissProgressbar();
    }

    @Override
    public void onFailedChangeAnswerResponse() {
        mResultsView.dismissProgressbar();
        mResultsView.showMessage(Alerts.SOMETHING_WRONG);
    }

    @Override
    public void StartProgressbar() {
        mResultsView.showProgressbar();
    }

    @Override
    public void onReplayPowerupApplied() {
        mResultsModel.callReplayPowerupApplied();
    }

    @Override
    public void onFlipPowerupApplied() {
        mResultsModel.showFlipQuestion();
    }

    @Override
    public void onClickShare() {
        mResultsView.takeScreenShot();
    }

    @Override
    public void onDoneScreenShot() {
        mResultsView.showProgressbar();
        mResultsModel.getShareText();
    }

    private void getResultDetails() {
        mResultsView.showProgressbar();
        mResultsModel.getMyResultsData(mResultsView.getContext());
    }

    @Override
    public void onSuccessMyResults(MyResultsAdapter myResultsAdapter) {
        mResultsView.dismissProgressbar();
        mResultsView.setAdapter(myResultsAdapter);
        mResultsView.setMatchName(mResultsModel.getMatchName());
    }

    @Override
    public void onFailedMyResults(String message) {
        mResultsView.dismissProgressbar();
        showAlertMessage(Alerts.RESULTS_INFO_ERROR);
    }

    @Override
    public void onNoInternet() {
        mResultsView.dismissProgressbar();
        showAlertMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    private void showAlertMessage(String message) {
        mResultsView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getResultDetails();
                    }
                });
    }

    @Override
    public void onEmpty() {
        mResultsView.dismissProgressbar();
        mResultsView.showInAppMessage(Alerts.NO_RESULTS_FOUND);
    }

    @Override
    public Context getContext() {
        return mResultsView.getContext();
    }

    @Override
    public void onFailedReplayPowerupResponse() {
        mResultsView.showMessage(Alerts.POWERUP_FAIL);
    }

    @Override
    public void onSuccessReplayPowerupResponse(Match match) {
        mResultsView.navigatetoPlay(match);
    }
}