package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.util.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 1/6/17.
 */

public class GroupSelectionPresenterImpl implements GroupSelectionPresenter, GroupSelectionModelImpl.OnGroupSelectionModelListener {

    private GroupSelectionView mGroupSelectionView;

    private GroupSelectionModel mGroupSelectionModel;

    private GroupSelectionPresenterImpl(GroupSelectionView groupSelectionView) {
        this.mGroupSelectionView = groupSelectionView;
        this.mGroupSelectionModel = GroupSelectionModelImpl.newInstance(this);
    }

    public static GroupSelectionPresenter newInstance(GroupSelectionView groupSelectionView) {
        return new GroupSelectionPresenterImpl(groupSelectionView);
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
    public void setTournamentsCount(int size, boolean isGroupTournamentChanged) {
        mGroupSelectionView.setTournamentsCount(size,isGroupTournamentChanged);
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
