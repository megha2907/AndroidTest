package in.sportscafe.scgame.module.feed;

import android.content.Context;
import android.view.View;

import in.sportscafe.scgame.Constants;

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
    public void onCreateFeed() {
        getFeedDetails();
    }

    private void getFeedDetails() {
        mFeedView.showProgressbar();
        mFeedModel.getFeeds();
    }

    @Override
    public void onSuccessFeeds(FeedAdapter feedAdapter, int movePosition) {
        mFeedView.dismissProgressbar();
        mFeedView.setAdapter(feedAdapter, movePosition);
    }

    @Override
    public void onFailedFeeds(String message) {
        mFeedView.dismissProgressbar();
        showAlertMessage(message);
    }

    @Override
    public void onNoInternet() {
        mFeedView.dismissProgressbar();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onEmpty() {
        mFeedView.dismissProgressbar();
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