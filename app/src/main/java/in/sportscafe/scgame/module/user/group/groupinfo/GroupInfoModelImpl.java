package in.sportscafe.scgame.module.user.group.groupinfo;

import android.content.Context;
import android.os.Bundle;

import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.group.LeaveGroupModelImpl;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.scgame.webservice.GroupSummaryResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
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

        Long groupId = Long.parseLong(bundle.getString(Constants.BundleKeys.GROUP_ID));

        getGroupSummary(groupId.intValue());
       // this.mGroupInfo = ScGameDataHandler.getInstance().getGrpInfoMap().get(groupId);

    }

    @Override
    public void updateGroupMembers(GroupInfo groupInfo){

        String myId = ScGameDataHandler.getInstance().getUserId();
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
                new ScGameCallBack<GroupSummaryResponse>() {
                    @Override
                    public void onResponse(Call<GroupSummaryResponse> call, Response<GroupSummaryResponse> response) {
                        if(response.isSuccessful()) {
                            ScGameDataHandler scGameDataHandler = ScGameDataHandler.getInstance();
                            GroupInfo groupInfo = response.body().getGroupInfo();

                            Map<Long, GroupInfo> grpInfoMap = scGameDataHandler.getGrpInfoMap();
                            grpInfoMap.put(groupInfo.getId(), groupInfo);

                            scGameDataHandler.setGrpInfoMap(grpInfoMap);
                            scGameDataHandler.setSelectedTournaments(groupInfo.getFollowedTournaments());
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
        bundle.putString(Constants.BundleKeys.GROUP_ID, String.valueOf(mGroupInfo.getId()));
        return bundle;
    }

    @Override
    public String getShareCodeContent() {
        return "To join my group " + mGroupInfo.getName() +
                " , use group code "+ mGroupInfo.getGroupCode();
    }

    @Override
    public void refreshGroupInfo() {
        this.mGroupInfo = ScGameDataHandler.getInstance().getGrpInfoMap().get(mGroupInfo.getId());
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
    public GroupTournamentAdapter getAdapter(Context context) {

        if(ScGameDataHandler.getInstance().getSelectedTournaments().isEmpty()){

            mGroupInfoModelListener.onEmptyList();
        }


        mGrpTournamentAdapter = new GroupTournamentAdapter(context,
                ScGameDataHandler.getInstance().getSelectedTournaments());
        return mGrpTournamentAdapter;
    }


    public interface OnGroupInfoModelListener {

        void onGroupNameUpdateSuccess();

        void onGroupNameEmpty();

        void onNoInternet();

        void onGroupTournamentUpdateSuccess();

        void onLeaveGroupSuccess();

        void onFailed(String message);

        void onGetGroupSummarySuccess(GroupInfo grpInfoList);

        void onGetGroupSummaryFailed(String message);

        Context getContext();

        void onEmptyList();
    }
}