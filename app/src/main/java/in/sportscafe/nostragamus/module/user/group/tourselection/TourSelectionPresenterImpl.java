package in.sportscafe.nostragamus.module.user.group.tourselection;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.module.user.myprofile.dto.TournamentFeedInfo;

/**
 * Created by deepanshi on 1/6/17.
 */
public class TourSelectionPresenterImpl implements TourSelectionPresenter, TourSelectionModelImpl.OnGroupSelectionModelListener {

    private TourSelectionFragment.OnTourSelectionListener mTourSelectionListener;

    private TourSelectionView mGroupSelectionView;

    private TourSelectionModel mGroupSelectionModel;

    private TourSelectionPresenterImpl(TourSelectionView groupSelectionView) {
        this.mGroupSelectionView = groupSelectionView;
        this.mGroupSelectionModel = TourSelectionModelImpl.newInstance(this);
    }

    public static TourSelectionPresenter newInstance(TourSelectionView groupSelectionView) {
        return new TourSelectionPresenterImpl(groupSelectionView);
    }

    @Override
    public void onCreateTourSelection(Bundle bundle, TourSelectionFragment.OnTourSelectionListener tourSelectionListener) {
        this.mTourSelectionListener = tourSelectionListener;
        mGroupSelectionModel.init(bundle);
        mGroupSelectionModel.getAllTournaments();
    }

    @Override
    public void onNoInternet() {
        mGroupSelectionView.dismissProgressbar();
        mGroupSelectionView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onGetAllToursSuccess(List<TournamentFeedInfo> allTours) {
        mGroupSelectionView.dismissProgressbar();
        mGroupSelectionView.setTourSelectionAdapter(mGroupSelectionModel.getTourSelectionAdapter(mGroupSelectionView.getContext(), allTours));
    }

    @Override
    public void onGetAllToursFailed(String message) {
        mGroupSelectionView.dismissProgressbar();
    }

    @Override
    public void selectedTournamentsLimit() {
        mGroupSelectionView.showMessage(Alerts.SELECTED_TOURNAMENTS_LIMIT);
    }

    @Override
    public void requireAdminAccess() {
        mGroupSelectionView.showMessage(Alerts.NOT_ADMIN);
    }

    @Override
    public void onUpdatingTourSelection() {
        mGroupSelectionView.showProgressbar();
    }

    @Override
    public void onTourUpdateSuccess(List<TournamentFeedInfo> followedTours) {
        mGroupSelectionView.dismissProgressbar();
        mTourSelectionListener.onTourSelectionChanged(followedTours);
    }

    @Override
    public void onTourUpdateFailed() {
        mGroupSelectionView.dismissProgressbar();
    }

}