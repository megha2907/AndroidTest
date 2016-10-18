package in.sportscafe.scgame.module.TournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;
import android.view.View;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.home.OnHomeActionListener;


/**
 * Created by Jeeva on 15/6/16.
 */
public class TournamentFeedPresenterImpl implements TournamentFeedPresenter, TournamentFeedModelImpl.OnTournamentFeedModelListener {

    private TournamentFeedView mTournamentFeedView;

    private TournamentFeedModel mTournamentFeedModel;

    private TournamentFeedPresenterImpl(TournamentFeedView tournamentFeedView) {
        this.mTournamentFeedView = tournamentFeedView;
        this.mTournamentFeedModel = TournamentFeedModelImpl.newInstance(this);
    }

    public static TournamentFeedPresenter newInstance(TournamentFeedView tournamentFeedView) {
        return new TournamentFeedPresenterImpl(tournamentFeedView);
    }

    @Override
    public void onCreateFeed(OnHomeActionListener listener) {
        mTournamentFeedView.setAdapter(mTournamentFeedModel.getAdapter(listener));
    }

    @Override
    public void onRefresh() {
        getFeedDetails();
    }

    private void getFeedDetails() {
        mTournamentFeedModel.getFeeds();
    }

    @Override
    public void onSuccessFeeds() {
        //mTournamentFeedView.moveAdapterPosition(movePosition);
        mTournamentFeedView.dismissSwipeRefresh();
    }

    @Override
    public void onFailedFeeds(String message) {
        mTournamentFeedView.dismissSwipeRefresh();
        showAlertMessage(message);
    }

    @Override
    public void onNoInternet() {
        mTournamentFeedView.dismissSwipeRefresh();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onEmpty() {
        mTournamentFeedView.dismissSwipeRefresh();
        mTournamentFeedView.showInAppMessage(Constants.Alerts.NO_FEEDS_FOUND);
    }

    @Override
    public Context getContext() {
        return mTournamentFeedView.getContext();
    }

    private void showAlertMessage(String message) {
        mTournamentFeedView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFeedDetails();
                    }
                });
    }


}
