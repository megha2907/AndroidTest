package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jeeva.android.ExceptionTracker;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

import static in.sportscafe.nostragamus.R.id.code;

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
        mGroupInfoView.setGroupName(mGroupInfoModel.getGroupName());
        getGroupDetails();
    }

    private void getGroupDetails() {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.getGroupDetails();
    }

    @Override
    public void onClickShareCode() {
        final GroupInfo groupInfo = mGroupInfoModel.getGroupInfo();

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();

        BranchUniversalObject buo = new BranchUniversalObject()
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata(BundleKeys.GROUP_CODE, groupInfo.getGroupCode())
                .addContentMetadata(BundleKeys.GROUP_NAME, groupInfo.getName())
                .addContentMetadata(BundleKeys.USER_REFERRAL_CODE, userInfo.getInfoDetails().getReferUserCode())
                .addContentMetadata(BundleKeys.WALLET_INITIAL_AMOUNT, String.valueOf(groupInfo.getWalletInitialAmount()))
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_PHOTO,userInfo.getPhoto())
                .addContentMetadata(Constants.BundleKeys.USER_REFERRAL_NAME, userInfo.getUserName());

        LinkProperties linkProperties = new LinkProperties();

        if (!TextUtils.isEmpty(groupInfo.getAppDownloadLink())) {
            linkProperties
                    .addTag("inviteGroup")
                    .setFeature("inviteGroup")
                    .setChannel("App")
                    .setCampaign("Group Invite")
                    .addControlParameter("$android_deeplink_path", "group/invite/")
                    .addControlParameter("$android_url", groupInfo.getAppDownloadLink());
        }else {
            linkProperties
                    .addTag("inviteGroup")
                    .setFeature("inviteGroup")
                    .setChannel("App")
                    .setCampaign("Group Invite")
                    .addControlParameter("$android_deeplink_path", "group/invite/");
        }

        buo.generateShortUrl(mGroupInfoView.getContext(), linkProperties,
                new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (null == error) {
                            NostragamusAnalytics.getInstance().trackGroups(AnalyticsActions.INVITE_GROUP);

                            String text = "I’m playing Nostragamus, the sports prediction app. Join my group ‘"
                                    + groupInfo.getName()
                                    +"’ and lets see who does better! Use my group code ‘"
                                    + groupInfo.getGroupCode()
                                    + "’ or click this link to download the app "
                                    + url;
                            AppSnippet.doGeneralShare(mGroupInfoView.getContext(), text);
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
    public void onClickEditGroup() {
        mGroupInfoView.navigateToEditGroup(mGroupInfoModel.getGroupInfoBundle());
    }

    @Override
    public void onNoInternet() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onClickLeaveGroup() {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.leaveGroup();
    }

    @Override
    public void onClickResetLb() {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.resetLeaderboard();
    }

    @Override
    public void onClickDeleteGroup() {
        mGroupInfoView.showProgressbar();
        mGroupInfoModel.deleteGroup();
    }

    @Override
    public void onClickBack() {
        if(mGroupInfoModel.isAnythingChanged()) {
            mGroupInfoView.setSuccessData(mGroupInfoModel.getGroupInfoBundle());
        }
        mGroupInfoView.goBack();
    }

    @Override
    public void onGetEditResult(Bundle bundle) {
        mGroupInfoModel.updateEditData(bundle);
        populateGroupInfo();
    }

    @Override
    public void onLeaveGroupSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Alerts.LEAVE_GROUP_SUCCESS);
        onClickBack();
    }

    @Override
    public void onResetLeaderboardSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Alerts.RESET_LB_SUCCESS);
    }

    @Override
    public void onFailed(String message) {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(message, Toast.LENGTH_LONG);
    }

    @Override
    public void onNoInternetForGroupDetails() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Alerts.NO_NETWORK_CONNECTION, "RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroupDetails();
            }
        });
    }

    @Override
    public void onTourTitleChanged(String title) {
        mGroupInfoView.setTourTabTitle(title);
    }

    @Override
    public void onMemberTitleChange(String title) {
        mGroupInfoView.setMemberTabTitle(title);
    }

    @Override
    public void onGetGroupInfoSuccess(GroupInfo groupInfo) {
        mGroupInfoView.dismissProgressbar();
        populateGroupInfo();
    }

    @Override
    public void onGetGroupInfoFailed(String message) {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(message);
        onClickBack();
    }

    @Override
    public void onDeleteGroupSuccess() {
        mGroupInfoView.dismissProgressbar();
        mGroupInfoView.showMessage(Alerts.DELETE_GROUP_SUCCESS);
        onClickBack();
    }

    private void populateGroupInfo() {
        GroupInfo groupInfo = mGroupInfoModel.getGroupInfo();
        mGroupInfoView.setGroupName(groupInfo.getName());
        mGroupInfoView.setGroupImage(groupInfo.getPhoto());
        mGroupInfoView.setAdapter(mGroupInfoModel.getAdapter(mGroupInfoView.getSupportFragmentManager()));

        if(mGroupInfoModel.amAdmin()) {
            mGroupInfoView.changeToAdminMode();
        }
    }
}