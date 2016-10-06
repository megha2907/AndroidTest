package in.sportscafe.scgame.module.user.group.groupinfo;

import android.os.Bundle;

import in.sportscafe.scgame.AppSnippet;
import in.sportscafe.scgame.Constants;
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
        mGroupInfoModel.init(bundle);

        GroupInfo groupInfo = mGroupInfoModel.getGroupInfo();
        mGroupInfoView.setGroupName(groupInfo.getName());
        mGroupInfoView.setGroupIcon(groupInfo.getName().substring(0,1));
        mGroupInfoView.setMembersSize(mGroupInfoModel.getMembersCount());
        mGroupInfoView.setAdapter(mGroupInfoModel.getAdapter(mGroupInfoView.getContext()));
        mGroupInfoView.setGroupCode(groupInfo.getGroupCode());

        if(mGroupInfoModel.amAdmin()) {
            mGroupInfoView.showDeleteGroup();
            mGroupInfoView.disableEdit();
        }
    }

    @Override
    public void onClickDeleteGroup() {

    }

    @Override
    public void onClickLeaveGroup() {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.leaveGroup();
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
    public void onDoneGroupName(String groupName) {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.updateGroupName(groupName);
        mGroupInfoView.setGroupIcon(groupName.substring(0,1));
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

    }

    @Override
    public void onGroupNameUpdateSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.disableEdit();

        mGroupInfoView.setSuccessResult();
    }
}