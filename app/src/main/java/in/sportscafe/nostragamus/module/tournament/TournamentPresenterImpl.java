package in.sportscafe.nostragamus.module.tournament;

import android.support.v4.app.FragmentManager;
import android.view.View;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by deepanshi on 11/14/16.
 */

public class TournamentPresenterImpl implements TournamentPresenter, TournamentModelImpl.OnTournamentModelListener {

    private TournamentView mtournamentView;

    private TournamentModel mTournamentModel;


    private TournamentPresenterImpl(TournamentView tournamentView, FragmentManager childFragmentManager) {
        this.mtournamentView = tournamentView;
        this.mTournamentModel = TournamentModelImpl.newInstance(this,childFragmentManager);
    }

    public static TournamentPresenterImpl newInstance(TournamentView pointsView, FragmentManager childFragmentManager) {
        return new TournamentPresenterImpl(pointsView,childFragmentManager);
    }


    @Override
    public void onCreateTournaments() {
        mtournamentView.showProgressbar();
        mTournamentModel.getTournaments();
    }

    @Override
    public void onNoInternet() {
        showAlertMsg(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onSuccessFeeds(String[] sportsArray) {
        mtournamentView.dismissProgressbar();
        if (null != mtournamentView.getContext()) {
            mtournamentView.initMyPosition(mTournamentModel.getAdapter(), mTournamentModel.getSelectedPosition(),sportsArray);
        }
    }

    @Override
    public void onFailedFeeds(String message) {
        mtournamentView.dismissProgressbar();
        showAlertMsg(message);
    }


    private void showAlertMsg(String message) {
        mtournamentView.dismissProgressbar();
        mtournamentView.showInAppMessage(Constants.Alerts.NO_FEEDS_FOUND);
    }

    private void refreshLb() {
        mtournamentView.showProgressbar();
        mTournamentModel.getTournaments();
    }

    @Override
    public void onEmpty() {
        mtournamentView.dismissProgressbar();
        mtournamentView.showInAppMessage("Your leaderboard will update here after a match you have played is over");
    }
}