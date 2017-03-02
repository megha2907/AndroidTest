package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import in.sportscafe.nostragamus.Constants;
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
        mResultsView.setMatchName(mResultsModel.getMatchName());

        if (bundle.containsKey("screen")) {
            mResultsView.goBack();
        }
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
//        mResultsView.navigatetoPlay(match);
    }

    @Override
    public void onScreenShotUploaded(String url) {
        mResultsView.dismissProgressbar();
        mResultsView.showFbShare(url);
    }

    @Override
    public void onScreenShotFailed() {
        mResultsView.dismissProgressbar();
        mResultsView.showMessage("Sharing failed, Try again", Toast.LENGTH_LONG);
    }

    @Override
    public void setToolbarHeading(String result) {
        mResultsView.setToolbarHeading(result);
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
    public void onClickFbShare() {
        mResultsView.takeScreenShot();
    }

    @Override
    public void onGetScreenShot(File screenshotFile) {
        mResultsView.showProgressbar();
        mResultsModel.uploadScreenShot(screenshotFile);
    }

    private void getResultDetails() {
        mResultsView.showProgressbar();
        mResultsModel.getMyResultsData(mResultsView.getContext());
    }

    @Override
    public void onSuccessMyResults(MyResultsAdapter myResultsAdapter) {
        mResultsView.dismissProgressbar();
        mResultsView.setAdapter(myResultsAdapter);
    }

    @Override
    public void onFailedMyResults(String message) {
        mResultsView.dismissProgressbar();
        showAlertMessage(Constants.Alerts.RESULTS_INFO_ERROR);
    }

    @Override
    public void onNoInternet() {
        mResultsView.dismissProgressbar();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
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
        mResultsView.showInAppMessage(Constants.Alerts.NO_RESULTS_FOUND);
    }

    @Override
    public Context getContext() {
        return mResultsView.getContext();
    }

    @Override
    public void gotoResultsTimeline() {
        mResultsView.goBack();
    }

    @Override
    public void onFailedReplayPowerupResponse() {
        mResultsView.showMessage(Constants.Alerts.POWERUP_FAIL);
    }

    @Override
    public void onSuccessReplayPowerupResponse(Match match) {
        mResultsView.navigatetoPlay(match);
    }
}