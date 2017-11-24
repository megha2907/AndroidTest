package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants;


/**
 * Created by Jeeva on 15/6/16.
 */
public class TimelinePresenterImpl implements TimelinePresenter, TimelineModelImpl.OnMyResultsTimelineModelListener {

    private TimelineView myResultsTimelineView;

    private TimelineModel myResultsTimelineModel;

    private TimelinePresenterImpl(TimelineView myResultsTimelineView) {
        this.myResultsTimelineView = myResultsTimelineView;
        this.myResultsTimelineModel = TimelineModelImpl.newInstance(this);
    }

    public static TimelinePresenter newInstance(TimelineView myResultsTimelineView) {
        return new TimelinePresenterImpl(myResultsTimelineView);
    }

    @Override
    public void onCreateFeed(Bundle bundle) {
        myResultsTimelineModel.init(bundle);
        myResultsTimelineView.setAdapter(myResultsTimelineModel.getAdapter(myResultsTimelineView.getContext()));

        getFeedDetails();
    }

    @Override
    public void onRefresh() {
        getFeedDetails();
    }

    @Override
    public void onTimelineScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        myResultsTimelineModel.checkPagination(firstVisibleItem, visibleItemCount, totalItemCount);
    }

    @Override
    public void onClickFilter() {
        myResultsTimelineModel.clearChallengeDetails();
        myResultsTimelineView.removeChallengeFilter();
        getFeedDetails();
    }

    @Override
    public void onDestroy() {
        myResultsTimelineModel.destroyAll();
    }

    private void getFeedDetails() {
        myResultsTimelineModel.getFeeds();
    }

    @Override
    public void onSuccessFeeds() {
//        myResultsTimelineView.moveAdapterPosition(movePosition);

        String challengeName = myResultsTimelineModel.getChallengeNameIfAvailable();
        if(null != challengeName) {
            myResultsTimelineView.setChallengeFilter(challengeName);
        }
    }

    @Override
    public void onFailedFeeds(String message) {
        showAlertMessage(message);
    }

    @Override
    public void onNoInternet() {
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onEmpty(Boolean isMyProfile) {
        if (myResultsTimelineModel.isAdapterEmpty()) {
            myResultsTimelineView.showTimelineEmpty(isMyProfile);
        }
    }

    @Override
    public boolean isThreadAlive() {
        return null != myResultsTimelineView.getContext();
    }

    @Override
    public void onAllTimelinesFetched() {

    }

    private void showAlertMessage(String message) {
        if (myResultsTimelineModel.isAdapterEmpty()) {
            myResultsTimelineView.showMessage(message, "RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFeedDetails();
                        }
                    });
        } else {
            myResultsTimelineView.showMessage(message);
        }
    }


}