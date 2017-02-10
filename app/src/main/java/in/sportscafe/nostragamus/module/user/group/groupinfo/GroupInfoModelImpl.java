package in.sportscafe.nostragamus.module.user.group.groupinfo;

import android.content.Context;
import android.os.Bundle;

import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.group.DeleteGroupModelImpl;
import in.sportscafe.nostragamus.module.user.group.LeaveGroupModelImpl;
import in.sportscafe.nostragamus.module.user.group.ResetLeaderboardModelImpl;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.webservice.GroupSummaryResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoModelImpl implements GroupInfoModel {

    private boolean mAdmin = false;

    private GroupInfo mGroupInfo;

    private GroupTournamentAdapter mGrpTournamentAdapter;

    private OnGroupInfoModelListener mGroupInfoModelListener;

    private GrpTournamentUpdateModelImpl mGrpTournamentUpdateModel;

    private GrpNameUpdateModelImpl mGrpNameUpdateModel;

    private GroupInfoModelImpl(OnGroupInfoModelListener listener) {
        this.mGroupInfoModelListener = listener;
    }

    public static GroupInfoModel newInstance(OnGroupInfoModelListener listener) {
        return new GroupInfoModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {

        if (bundle.getInt(Constants.BundleKeys.GROUP_ID) != 0) {
            Integer groupId = (bundle.getInt(Constants.BundleKeys.GROUP_ID));
            getGroupSummary(groupId);
        } else {
            mGroupInfoModelListener.onFailed(Constants.Alerts.GROUP_INFO_ERROR);
            mGroupInfoModelListener.gotoAllGroupsScreen();
        }
    }

    @Override
    public void updateGroupMembers(GroupInfo groupInfo) {
        String myId = NostragamusDataHandler.getInstance().getUserId();
        List<GroupPerson> groupMembers = groupInfo.getMembers();

        for (GroupPerson groupPerson : groupMembers) {
            if (myId.compareTo(groupPerson.getId().toString()) == 0
                    && groupPerson.isAdmin()) {
                mAdmin = true;
                break;
            }
        }
    }


    private void getGroupSummary(Integer GroupId) {
        MyWebService.getInstance().getGroupSummaryRequest(GroupId).enqueue(
                new NostragamusCallBack<GroupSummaryResponse>() {
                    @Override
                    public void onResponse(Call<GroupSummaryResponse> call, Response<GroupSummaryResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();
                            GroupInfo groupInfo = response.body().getGroupInfo();

                            Map<Integer, GroupInfo> grpInfoMap = nostragamusDataHandler.getGrpInfoMap();
                            grpInfoMap.put(groupInfo.getId(), groupInfo);

                            nostragamusDataHandler.setGrpInfoMap(grpInfoMap);
                            nostragamusDataHandler.setSelectedTournaments(groupInfo.getFollowedTournaments());
                            groupInfo.setMembers(groupInfo.getMembers());

                            mGroupInfo = groupInfo;
                            mGroupInfoModelListener.onGetGroupSummarySuccess(groupInfo);
                        } else {
                            mGroupInfoModelListener.onGetGroupSummaryFailed(response.message());
                        }
                    }
                }
        );
    }

    @Override
    public boolean amAdmin() {
        return mAdmin;
    }

    @Override
    public GroupInfo getGroupInfo() {
        return mGroupInfo;
    }

    @Override
    public int getMembersCount() {
        int membersCount = 0;
        for (GroupPerson groupPerson : mGroupInfo.getMembers()) {
            if (groupPerson.isApproved()) {
                membersCount++;
            }
        }
        return membersCount;
    }


    @Override
    public Bundle getGroupIdBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.GROUP_ID, mGroupInfo.getId());
        return bundle;
    }

    @Override
    public String getShareCodeContent() {
        return "To join my group " + mGroupInfo.getName() +
                " , use group code " + mGroupInfo.getGroupCode();
    }

    @Override
    public void refreshGroupInfo() {
        this.mGroupInfo = NostragamusDataHandler.getInstance().getGrpInfoMap().get(mGroupInfo.getId());
    }

    @Override
    public void leaveGroup() {
        new LeaveGroupModelImpl(new LeaveGroupModelImpl.OnLeaveGroupModelListener() {
            @Override
            public void onSuccessLeaveGroup() {
                mGroupInfoModelListener.onLeaveGroupSuccess();
            }

            @Override
            public void onFailedLeaveGroup(String message) {
                mGroupInfoModelListener.onFailed(message);
            }

            @Override
            public void onNoInternet() {
                mGroupInfoModelListener.onNoInternet();
            }
        }).leaveGroup(mAdmin, mGroupInfo.getId());
    }

    @Override
    public void resetLeaderboard() {
        new ResetLeaderboardModelImpl(new ResetLeaderboardModelImpl.OnResetLeaderboardModelListener() {
            @Override
            public void onSuccessResetLeaderboard() {
                mGroupInfoModelListener.onResetLeaderboardSuccess();
            }

            @Override
            public void onFailedResetLeaderboard(String message) {
                mGroupInfoModelListener.onFailed(message);
            }

            @Override
            public void onNoInternet() {
                mGroupInfoModelListener.onNoInternet();
            }
        }).resetLeaderboard(mGroupInfo.getId());
    }

    @Override
    public void deleteGroup() {
        new DeleteGroupModelImpl(new DeleteGroupModelImpl.OnDeleteGroupModelListener() {
            @Override
            public void onSuccessDeleteGroup() {
                mGroupInfoModelListener.onDeleteGroupSuccess();
            }

            @Override
            public void onFailedDeleteGroup(String message) {
                mGroupInfoModelListener.onFailed(message);
            }

            @Override
            public void onNoInternet() {
                mGroupInfoModelListener.onNoInternet();
            }
        }).deleteGroup(mAdmin, mGroupInfo.getId());
    }

    @Override
    public GroupTournamentAdapter getAdapter(Context context) {

        if (NostragamusDataHandler.getInstance().getSelectedTournaments().isEmpty()) {
            mGroupInfoModelListener.onEmptyList();
        }

        mGrpTournamentAdapter = new GroupTournamentAdapter(context,
                NostragamusDataHandler.getInstance().getSelectedTournaments());
        return mGrpTournamentAdapter;
    }


    public interface OnGroupInfoModelListener {

        void onGroupNameUpdateSuccess();

        void onGroupNameEmpty();

        void onNoInternet();

        void onGroupTournamentUpdateSuccess();

        void onLeaveGroupSuccess();

        void onResetLeaderboardSuccess();

        void onFailed(String message);

        void gotoAllGroupsScreen();

        void onGetGroupSummarySuccess(GroupInfo grpInfoList);

        void onGetGroupSummaryFailed(String message);

        Context getContext();

        void onEmptyList();

        void onDeleteGroupSuccess();
    }
}