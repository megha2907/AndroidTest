package in.sportscafe.nostragamus.module.tournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.Alerts;

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
    public void onCreateFeed(Bundle bundle) {
        mTournamentFeedModel.init(bundle);
        mTournamentFeedView.setAdapter(mTournamentFeedModel.getAdapter(mTournamentFeedView.getContext()));
    }

    @Override
    public void onEmpty() {
        mTournamentFeedView.showInAppMessage(Alerts.NO_FEEDS_FOUND);
    }

}