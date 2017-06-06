package in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist;

import android.support.v4.app.FragmentManager;
import android.view.View;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */
public class TourListPresenterImpl implements TourListPresenter, TourListModelImpl.OnTournamentModelListener {

    private TourListView mTournamentView;

    private TourListModel mTournamentModel;

    private TourListPresenterImpl(TourListView tournamentView, FragmentManager childFragmentManager) {
        this.mTournamentView = tournamentView;
        this.mTournamentModel = TourListModelImpl.newInstance(this, childFragmentManager);
    }

    public static TourListPresenterImpl newInstance(TourListView pointsView, FragmentManager childFragmentManager) {
        return new TourListPresenterImpl(pointsView, childFragmentManager);
    }

    @Override
    public void onCreateTournaments() {
        mTournamentModel.getTourList();
        if (!NostragamusDataHandler.getInstance().isVisitedSubmitQuestion()) {
            mTournamentView.showExplainSubmitQuesPopUp();
        }
    }

    @Override
    public boolean onApiCallStopped() {
        return mTournamentView.dismissProgressbar();
    }

    @Override
    public void onApiCallStarted() {
        mTournamentView.showProgressbar();
    }

    @Override
    public void onNoInternet() {
        mTournamentView.dismissProgressbar();
        showAlertMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onSuccessTourList(ViewPagerAdapter adapter) {
        mTournamentView.dismissProgressbar();
        if (null != mTournamentView.getContext()) {
            mTournamentView.setAdapter(adapter);
        }
    }

    @Override
    public void onFailedTourList() {
        mTournamentView.dismissProgressbar();
        showAlertMsg(Alerts.API_FAIL);
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

    private void showAlertMessage(String message) {
        mTournamentView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTournamentModel.getTourList();
                    }
                });
    }
}