package in.sportscafe.nostragamus.module.tournament;

import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */

public class TournamentPresenterImpl implements TournamentPresenter, TournamentModelImpl.OnTournamentModelListener {

    private TournamentView mTournamentView;

    private TournamentModel mTournamentModel;

    private TournamentPresenterImpl(TournamentView tournamentView, FragmentManager childFragmentManager) {
        this.mTournamentView = tournamentView;
        this.mTournamentModel = TournamentModelImpl.newInstance(this, childFragmentManager);
    }

    public static TournamentPresenterImpl newInstance(TournamentView pointsView, FragmentManager childFragmentManager) {
        return new TournamentPresenterImpl(pointsView, childFragmentManager);
    }

    @Override
    public void onCreateTournaments() {
        mTournamentView.showProgressbar();
        mTournamentModel.getTournaments();
    }

    @Override
    public void onNoInternet() {
        mTournamentView.dismissProgressbar();
        mTournamentView.showMessage(Alerts.NO_NETWORK_CONNECTION, Toast.LENGTH_LONG);
    }

    @Override
    public void onSuccessFeeds(ViewPagerAdapter adapter) {
        mTournamentView.dismissProgressbar();
        if (null != mTournamentView.getContext()) {
            mTournamentView.setAdapter(adapter);
        }
    }

    @Override
    public void onFailedFeeds(String message) {
        mTournamentView.dismissProgressbar();
        showAlertMsg(message);
    }

    private void showAlertMsg(String message) {
        mTournamentView.dismissProgressbar();
        mTournamentView.showInAppMessage(Alerts.NO_FEEDS_FOUND);
    }

    @Override
    public void onEmpty() {
        mTournamentView.dismissProgressbar();
        mTournamentView.showInAppMessage(Alerts.NO_FEEDS_FOUND);
    }
}