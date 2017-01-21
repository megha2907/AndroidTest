package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.jeeva.android.ExceptionTracker;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

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
        mGroupInfoView.setGroupName(bundle.getString(BundleKeys.GROUP_NAME));
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.init(bundle);
    }


    private void onUpdateGroupInfo(GroupInfo groupInfo){

        mGroupInfoView.setGroupName(groupInfo.getName());
        mGroupInfoView.setGroupIcon(groupInfo.getPhoto());
        mGroupInfoView.setMembersSize(mGroupInfoModel.getMembersCount());
       // mGroupInfoView.setAdapter(mGroupInfoModel.getSelectedAdapter(mGroupInfoView.isThreadAlive()));
        mGroupInfoView.setGroupCode(groupInfo.getGroupCode());
        mGroupInfoView.initMyPosition(groupInfo);

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
        GroupInfo groupInfo = mGroupInfoModel.getGroupInfo();

        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle("Play Nostragamus")
                .setContentDescription("I challenge you - Click this link to join the &quot;" + groupInfo.getName() + "&quot; and prove your love of sports" )
                .setContentImageUrl("https://cdn-images.spcafe.in/img/es3/screact/game-app/game-logo.png")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(BundleKeys.GROUP_CODE, groupInfo.getGroupCode())
                .addContentMetadata(BundleKeys.GROUP_NAME, groupInfo.getName())
                .addContentMetadata(BundleKeys.USER_REFERRAL_ID, NostragamusDataHandler.getInstance().getUserId());


        LinkProperties linkProperties = new LinkProperties()
                .addTag("inviteGroup")
                .setFeature("inviteGroup")
                .addControlParameter("$android_deeplink_path", "group/invite/");

        buo.generateShortUrl(mGroupInfoView.getContext(), linkProperties,
                new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if(null == error) {
                    AppSnippet.doGeneralShare(mGroupInfoView.getContext(), url);
                } else {
                    ExceptionTracker.track(error.getMessage());
                }
            }
        });
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
    public void onLeaveGroup() {
        mGroupInfoModel.leaveGroup();
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

    @Override
    public void gotoAllGroupsScreen() {
        mGroupInfoView.navigateToAllGroups();
    }

    private void showAlert(String message) {
        Toast.makeText(mGroupInfoView.getContext(), message, Toast.LENGTH_SHORT).show();

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
