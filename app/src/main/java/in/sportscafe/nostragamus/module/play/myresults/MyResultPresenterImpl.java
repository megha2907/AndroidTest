package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
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

        if (bundle.containsKey("screen"))
        {
            mResultsView.goBack();
        }

    }

    @Override
    public void onArticleScroll(int firstVisibleItemPosition, int childCount, int itemCount) {
        mResultsModel.checkPagination(firstVisibleItemPosition, childCount, itemCount);
    }

    @Override
    public void onPowerUp(String powerup) {
        if(powerup.equalsIgnoreCase("match_replay")){
            mResultsView.openDialog();
        }
    }

    @Override
    public void onReplayPowerupApplied() {
        mResultsModel.callReplayPowerupApplied();
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
        showAlertMessage(message);
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
        mResultsView.showMessage(Constants.Alerts.REPLAY_POWERUP_FAIL);
    }

    @Override
    public void onSuccessReplayPowerupResponse(Match match) {
        mResultsView.navigatetoPlay(match);
    }
}