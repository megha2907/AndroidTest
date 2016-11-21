package in.sportscafe.scgame.module.tournamentFeed;

/**
 * Created by deepanshi on 9/29/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.home.OnHomeActionListener;


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
    public void onCreateFeed(OnHomeActionListener listener,Bundle bundle) {
        mTournamentFeedView.setAdapter(mTournamentFeedModel.getAdapter(listener));
        mTournamentFeedModel.init(bundle);
    }


    @Override
    public void update(Bundle bundle) {
        mTournamentFeedModel.getTournamentFeed(bundle);
    }


    @Override
    public void onEmpty() {
        mTournamentFeedView.showInAppMessage(Constants.Alerts.NO_FEEDS_FOUND);
    }

    @Override
    public Context getContext() {
        return mTournamentFeedView.getContext();
    }



}
