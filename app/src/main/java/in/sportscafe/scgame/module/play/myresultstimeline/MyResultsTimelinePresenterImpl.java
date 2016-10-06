package in.sportscafe.scgame.module.play.myresultstimeline;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import in.sportscafe.scgame.Constants;


/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsTimelinePresenterImpl implements MyResultsTimelinePresenter, MyResultsTimelineModelImpl.OnMyResultsTimelineModelListener {

    private MyResultsTimelineView myResultsTimelineView;

    private MyResultsTimelineModel myResultsTimelineModel;

    private MyResultsTimelinePresenterImpl(MyResultsTimelineView myResultsTimelineView) {
        this.myResultsTimelineView = myResultsTimelineView;
        this.myResultsTimelineModel = MyResultsTimelineModelImpl.newInstance(this);
    }

    public static MyResultsTimelinePresenter newInstance(MyResultsTimelineView myResultsTimelineView) {
        return new MyResultsTimelinePresenterImpl(myResultsTimelineView);
    }

    @Override
    public void onCreateFeed() {
        myResultsTimelineView.setAdapter(myResultsTimelineModel.getAdapter());
    }

    @Override
    public void onRefresh() {
        getFeedDetails();
    }

    private void getFeedDetails() {
        myResultsTimelineModel.getFeeds();
    }

    @Override
    public void onSuccessFeeds(MyResultsTimelineAdapter myResultsTimelineAdapter, int movePosition) {
        myResultsTimelineView.moveAdapterPosition(movePosition);
        myResultsTimelineView.dismissSwipeRefresh();
    }

    @Override
    public void onFailedFeeds(String message) {
        myResultsTimelineView.dismissSwipeRefresh();
        showAlertMessage(message);
    }

    @Override
    public void onNoInternet() {
        myResultsTimelineView.dismissSwipeRefresh();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onEmpty() {
        myResultsTimelineView.dismissSwipeRefresh();
        myResultsTimelineView.showInAppMessage(Constants.Alerts.NO_FEEDS_FOUND);
    }

    @Override
    public Context getContext() {
        return myResultsTimelineView.getContext();
    }

    private void showAlertMessage(String message) {
        myResultsTimelineView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFeedDetails();
                    }
                });
    }


}