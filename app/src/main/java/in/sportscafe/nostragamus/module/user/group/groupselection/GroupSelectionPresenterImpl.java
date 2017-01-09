package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.io.File;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoModel;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoModelImpl;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoPresenter;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoPresenterImpl;
import in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoView;
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

        mGroupSelectionView.setAdapter(mGroupSelectionModel.getAdapter(mGroupSelectionView.getContext()));

    }


    @Override
    public void onDoneUpdateTournaments() {
        mGroupSelectionModel.updateTournaments();

    }



    @Override
    public void onGetMemberResult() {
        mGroupSelectionModel.refreshGroupInfo();
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
    public Context getContext() {
        return mGroupSelectionView.getContext();
    }

    @Override
    public void onUpdating() {
        mGroupSelectionView.showProgressbar();
    }

    @Override
    public void onEditFailed(String message) {
        mGroupSelectionView.dismissProgressbar();
        mGroupSelectionView.showMessage(message);
    }

    @Override
    public void onSuccessTournamentInfo() {
        mGroupSelectionView.dismissProgressbar();
    }
}
