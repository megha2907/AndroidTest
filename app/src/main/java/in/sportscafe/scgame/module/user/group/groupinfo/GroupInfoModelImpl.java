package in.sportscafe.scgame.module.user.group.groupinfo;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.user.group.LeaveGroupModelImpl;
import in.sportscafe.scgame.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoModelImpl implements GroupInfoModel {

    private boolean mAdmin = false;

    private GroupInfo mGroupInfo;

    private GrpTournamentSelectionAdapter mGrpTournamentSelectionAdapter;

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
        Long groupId = bundle.getLong(Constants.BundleKeys.GROUP_ID);
        this.mGroupInfo = ScGameDataHandler.getInstance().getGrpInfoMap().get(groupId);

        String myId = ScGameDataHandler.getInstance().getUserId();
        List<GroupPerson> groupMembers = mGroupInfo.getMembers();

        for (GroupPerson groupPerson : groupMembers) {
            if (myId.compareTo(groupPerson.getId().toString()) == 0
                    && groupPerson.isAdmin()) {
                mAdmin = true;
                break;
            }
        }

        this.mGrpTournamentUpdateModel = new GrpTournamentUpdateModelImpl(mGroupInfo.getId(),
                new GrpTournamentUpdateModelImpl.OnGrpTournamentUpdateModelListener() {
                    @Override
                    public void onSuccessGrpTournamentUpdate() {
                        mGroupInfoModelListener.onGroupTournamentUpdateSuccess();
                    }

                    @Override
                    public void onFailedGrpTournamentUpdate(String message) {}

                    @Override
                    public void onNoInternet() {}
                });

        this.mGrpNameUpdateModel = new GrpNameUpdateModelImpl(mGroupInfo.getId(),
                new GrpNameUpdateModelImpl.OnGrpNameUpdateModelListener() {
                    @Override
                    public void onSuccessGrpNameUpdate() {
                        mGroupInfoModelListener.onGroupNameUpdateSuccess();
                    }

                    @Override
                    public void onFailedGrpNameUpdate(String message) {}

                    @Override
                    public void onNoInternet() {
                        mGroupInfoModelListener.onNoInternet();
                    }
                });
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
    public GrpTournamentSelectionAdapter getAdapter(Context context) {
        List<TournamentInfo> followedTournaments = mGroupInfo.getFollowedTournaments();

        List<Integer> mFollowedTournamentsIdList = new ArrayList<>();
        for (TournamentInfo tournamentInfo : followedTournaments) {
            mFollowedTournamentsIdList.add(tournamentInfo.getTournamentId());
        }

        this.mGrpTournamentSelectionAdapter = new GrpTournamentSelectionAdapter(context,
                mFollowedTournamentsIdList, new GrpTournamentSelectionAdapter.OnGrpTournamentChangedListener() {

            @Override
            public boolean onGrpTournamentSelected(boolean addNewTournament, int existingTournamentCount) {
                return mAdmin && (addNewTournament || existingTournamentCount > 1);
            }

            @Override
            public void onGrpTournamentChanged(List<Integer> selectedTournamentsIdList) {
                mGrpTournamentUpdateModel.updateGrpTournaments(selectedTournamentsIdList);
            }

        });

        if(amAdmin()) {
            this.mGrpTournamentSelectionAdapter.addAll(ScGameDataHandler.getInstance().getTournaments());
        } else {
            this.mGrpTournamentSelectionAdapter.addAll(followedTournaments);
        }
        return mGrpTournamentSelectionAdapter;
    }

    @Override
    public Bundle getGroupIdBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BundleKeys.GROUP_ID, mGroupInfo.getId());
        return bundle;
    }

    @Override
    public void updateGroupName(String groupName) {
        if (groupName.isEmpty()) {
            mGroupInfoModelListener.onGroupNameEmpty();
            return;
        }

        mGrpNameUpdateModel.updateGrpName(groupName);
    }

    @Override
    public String getShareCodeContent() {
        return "Group Code: " + mGroupInfo.getGroupCode() + "\n\n" +
                "If you want to join in the '" + mGroupInfo.getName() +
                "' group, Use the above code in the join group page in the application. " +
                "\n\nIf you don't have the application, use this link";
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

    public interface OnGroupInfoModelListener {

        void onGroupNameUpdateSuccess();

        void onGroupNameEmpty();

        void onNoInternet();

        void onGroupTournamentUpdateSuccess();

        void onLeaveGroupSuccess();

        void onFailed(String message);
    }
}