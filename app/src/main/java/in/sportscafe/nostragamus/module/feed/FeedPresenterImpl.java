package in.sportscafe.nostragamus.module.feed;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;

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
    public void onCreateFeed(Bundle bundle) {
        mFeedModel.init(bundle);
        getFeedDetails();
    }

    @Override
    public void onRefresh() {
        getFeedDetails();
    }

    @Override
    public void onDestroy() {
        mFeedModel.destroyAll();
    }

    private void getFeedDetails() {
        mFeedModel.getFeeds();
    }

    @Override
    public void onSuccessFeeds(List<Match> matchList) {
        mFeedView.setAdapter(mFeedModel.getAdapter());
        mFeedModel.handleMatches(matchList);
        mFeedView.moveAdapterPosition(mFeedModel.getAdapter().getItemCount() - 1);

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
    public void onEmpty() {
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