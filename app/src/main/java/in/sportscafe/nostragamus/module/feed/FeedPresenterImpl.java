package in.sportscafe.nostragamus.module.feed;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.feed.dto.Match;

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

        mFeedView.setTournamentName(mFeedModel.getTournamentName());
    }

    @Override
    public void onRefresh() {
        mFeedView.hidePowerups();
        mFeedView.showProgressbar();
        mFeedModel.refreshFeed();
    }

    @Override
    public void onBack() {
        if(mFeedModel.isFeedRefreshed()) {
            mFeedView.navigateToHome();
        } else {
            mFeedView.goBack();
        }
    }

    @Override
    public void onDestroy() {
        mFeedModel.destroyAll();
    }

    private void getFeedDetails() {
        mFeedView.showProgressbar();
        mFeedModel.getFeeds();
    }

    @Override
    public void onSuccessFeeds(List<Match> matchList, Integer powerUp2x, Integer powerUpNonEgs,
                               Integer powerUpAudiencePoll, String powerupText) {
        mFeedView.dismissProgressbar();

        if (null != mFeedView.getContext()) {
            mFeedView.setAdapter(mFeedModel.getAdapter(mFeedView.getContext(), matchList));
            mFeedView.moveAdapterPosition(matchList.size() - 1);

            mFeedView.showPowerups(powerUp2x, powerUpNonEgs, powerUpAudiencePoll, powerupText);
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
    public void onEmpty() {
        mFeedView.showInAppMessage(Constants.Alerts.NO_FEEDS_FOUND);
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