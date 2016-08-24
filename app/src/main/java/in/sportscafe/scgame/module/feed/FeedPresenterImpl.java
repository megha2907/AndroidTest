package in.sportscafe.scgame.module.feed;

import android.content.Context;
import android.view.View;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.home.OnHomeActionListener;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedPresenterImpl implements FeedPresenter, FeedModelImpl.OnFeedModelListener {

    private FeedView mFeedView;

    private FeedModel mFeedModel;

    private FeedPresenterImpl(FeedView feedView) {
        this.mFeedView = feedView;
        this.mFeedModel = FeedModelImpl.newInstance(this);
    }

    public static FeedPresenter newInstance(FeedView feedView) {
        return new FeedPresenterImpl(feedView);
    }

    @Override
    public void onCreateFeed(OnHomeActionListener listener) {
        mFeedView.setAdapter(mFeedModel.getAdapter(listener));
    }

    @Override
    public void onRefresh() {
        getFeedDetails();
    }

    private void getFeedDetails() {
        mFeedModel.getFeeds();
    }

    @Override
    public void onSuccessFeeds(FeedAdapter feedAdapter, int movePosition) {
        mFeedView.moveAdapterPosition(movePosition);
        mFeedView.dismissSwipeRefresh();
    }

    @Override
    public void onFailedFeeds(String message) {
        mFeedView.dismissSwipeRefresh();
        showAlertMessage(message);
    }

    @Override
    public void onNoInternet() {
        mFeedView.dismissSwipeRefresh();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onEmpty() {
        mFeedView.dismissSwipeRefresh();
        mFeedView.showInAppMessage(Constants.Alerts.NO_FEEDS_FOUND);
    }

    @Override
    public Context getContext() {
        return mFeedView.getContext();
    }

    private void showAlertMessage(String message) {
        mFeedView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFeedDetails();
                    }
                });
    }


}