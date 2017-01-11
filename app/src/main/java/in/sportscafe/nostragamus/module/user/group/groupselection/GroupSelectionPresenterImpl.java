package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.content.Context;
import android.util.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.sportselection.profilesportselection.ProfileSportSelectionFragment;

/**
 * Created by deepanshi on 1/6/17.
 */

public class GroupSelectionPresenterImpl implements GroupSelectionPresenter, GroupSelectionModelImpl.OnGroupSelectionModelListener {

    private GroupSelectionView mGroupSelectionView;

    private GroupSelectionModel mGroupSelectionModel;

    private GroupSelectionFragment.OnTournamentUpdatedListener mChangedListener;

    private GroupSelectionPresenterImpl(GroupSelectionView groupSelectionView,GroupSelectionFragment.OnTournamentUpdatedListener listener) {
        this.mGroupSelectionView = groupSelectionView;
        this.mGroupSelectionModel = GroupSelectionModelImpl.newInstance(this);
        this.mChangedListener =listener;
    }

    public static GroupSelectionPresenter newInstance(GroupSelectionView groupSelectionView,GroupSelectionFragment.OnTournamentUpdatedListener listener) {
        return new GroupSelectionPresenterImpl(groupSelectionView,listener);
    }

    @Override
    public void onGetGroupSelectionInfo(long groupId) {

        mGroupSelectionView.showProgressbar();
        mGroupSelectionModel.init(groupId);
    }

    private void onUpdateGroupSelectionInfo(GroupInfo groupInfo){

        Log.i("inside","onUpdateGroupSelectionInfo");
        mGroupSelectionModel.getAllTournamentsfromServer();

    }

    @Override
    public void onNoInternet() {
        mGroupSelectionView.dismissProgressbar();
        mGroupSelectionView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onGroupTournamentUpdateSuccess() {
        mGroupSelectionView.setSuccessResult();
    }


    @Override
    public void onFailed(String message) {
        mGroupSelectionView.dismissProgressbar();
    }


    @Override
    public void onGetGroupSummarySuccess(GroupInfo groupInfo) {
        mGroupSelectionView.dismissProgressbar();
        mGroupSelectionModel.updateGroupMembers();
        mGroupSelectionModel.updateTournaments();
        onUpdateGroupSelectionInfo(groupInfo);
    }

    @Override
    public void onGetGroupSummaryFailed(String message) {

    }

    @Override
    public void onSuccessTournamentInfo(List<TournamentFeedInfo> tournamentInfos) {
        mGroupSelectionView.dismissProgressbar();
        mGroupSelectionView.setSelectedAdapter(mGroupSelectionModel.getSelectedAdapter(mGroupSelectionView.getContext(), tournamentInfos));
    }

    @Override
    public void setTournamentsCount(int size) {
        mChangedListener.setTournamentsCount(size);
    }

    @Override
    public void selectedTournamentsLimit() {
        mGroupSelectionView.showMessage(Constants.Alerts.SELECTED_TOURNAMENTS_LIMIT);
    }

    @Override
    public void notanAdmin() {
        mGroupSelectionView.showMessage(Constants.Alerts.NOT_ADMIN);
    }

}
