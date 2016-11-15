package in.sportscafe.scgame.module.tournament;

import android.support.v4.app.FragmentManager;
import android.view.View;

import in.sportscafe.scgame.Constants;

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
    public void onSuccessFeeds() {
        mtournamentView.dismissProgressbar();
        mtournamentView.initMyPosition(mTournamentModel.getAdapter(), mTournamentModel.getSelectedPosition());
    }

    @Override
    public void onFailedFeeds(String message) {
        mtournamentView.dismissProgressbar();
        showAlertMsg(message);
    }


    private void showAlertMsg(String message) {
        mtournamentView.dismissProgressbar();
        mtournamentView.showMessage(message, "RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLb();
            }
        });
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