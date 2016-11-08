package in.sportscafe.scgame.module.user.group.groupinfo;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.scgame.AppSnippet;
import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoPresenterImpl implements GroupInfoPresenter, GroupInfoModelImpl.OnGroupInfoModelListener {

    private GroupInfoView mGroupInfoView;

    private GroupInfoModel mGroupInfoModel;

    private GroupInfoPresenterImpl(GroupInfoView groupInfoView) {
        this.mGroupInfoView = groupInfoView;
        this.mGroupInfoModel = GroupInfoModelImpl.newInstance(this);
    }

    public static GroupInfoPresenter newInstance(GroupInfoView groupInfoView) {
        return new GroupInfoPresenterImpl(groupInfoView);
    }

    @Override
    public void onCreateGroupInfo(Bundle bundle) {
        mGroupInfoView.setGroupName(bundle.getString(Constants.BundleKeys.GROUP_NAME));
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.init(bundle);

    }


    private void onUpdateGroupInfo(GroupInfo groupInfo){

        mGroupInfoView.setGroupName(groupInfo.getName());
        mGroupInfoView.setGroupIcon(groupInfo.getPhoto());
        mGroupInfoView.setMembersSize(mGroupInfoModel.getMembersCount());
        mGroupInfoView.setAdapter(mGroupInfoModel.getAdapter(mGroupInfoView.getContext()));
        mGroupInfoView.setGroupCode(groupInfo.getGroupCode());

//        if(mGroupInfoModel.amAdmin()) {
//            mGroupInfoView.showDeleteGroup();
//           // mGroupInfoView.disableEdit();
//        }

    }

    @Override
    public void onClickMembers() {
        Bundle bundle = mGroupInfoModel.getGroupIdBundle();
        if(mGroupInfoModel.amAdmin()) {
            mGroupInfoView.navigateToAdminMembers(bundle);
        } else {
            mGroupInfoView.navigateToGroupMembers(bundle);
        }
    }


    @Override
    public void onClickShareCode() {
        AppSnippet.doGeneralShare(mGroupInfoView.getContext(), mGroupInfoModel.getShareCodeContent());
    }

    @Override
    public void onLongClickShareCode() {
        AppSnippet.copyToClipBoard(mGroupInfoView.getContext(), mGroupInfoModel.getGroupInfo().getGroupCode());
    }

    @Override
    public void onGetMemberResult() {
        mGroupInfoModel.refreshGroupInfo();
        mGroupInfoView.setMembersSize(mGroupInfoModel.getMembersCount());
    }

    @Override
    public void onGroupNameEmpty() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Constants.Alerts.EMPTY_GROUP_NAME);
    }

    @Override
    public void onNoInternet() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onGroupTournamentUpdateSuccess() {
        mGroupInfoView.setSuccessResult();
    }

    @Override
    public void onLeaveGroupSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Constants.Alerts.LEAVE_GROUP_SUCCESS);
        mGroupInfoView.navigateToHome();
    }

    @Override
    public void onFailed(String message) {
        mGroupInfoView.dismissProgressbar();
        showAlert(message);

    }

    private void showAlert(String message) {

        }


    @Override
    public void onGetGroupSummarySuccess(GroupInfo groupInfo) {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoModel.updateGroupMembers(groupInfo);
        onUpdateGroupInfo(groupInfo);

    }

    @Override
    public void onGetGroupSummaryFailed(String message) {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(message);
    }

    @Override
    public Context getContext() {
        return mGroupInfoView.getContext();
    }

    @Override
    public void onEmptyList() {

    }

    @Override
    public void onGroupNameUpdateSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.disableEdit();
        mGroupInfoView.setSuccessResult();
    }
}