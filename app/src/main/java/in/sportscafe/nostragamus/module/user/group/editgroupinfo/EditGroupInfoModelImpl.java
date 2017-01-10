package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.user.group.LeaveGroupModelImpl;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GrpNameUpdateModelImpl;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GrpTournamentUpdateModelImpl;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.webservice.GroupSummaryResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditGroupInfoModelImpl implements EditGroupInfoModel {

    private boolean mAdmin = false;

    private GroupInfo mGroupInfo;

    private GrpTournamentSelectionAdapter mGrpTournamentSelectionAdapter;

    private OnEditGroupInfoModelListener mGroupInfoModelListener;

    private GrpTournamentUpdateModelImpl mGrpTournamentUpdateModel;

    private GrpNameUpdateModelImpl mGrpNameUpdateModel;

    private NostragamusDataHandler mNostragamusDataHandler;

    private EditGroupInfoModelImpl(OnEditGroupInfoModelListener listener) {
        this.mGroupInfoModelListener = listener;
        mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static EditGroupInfoModel newInstance(OnEditGroupInfoModelListener listener) {
        return new EditGroupInfoModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        Long groupId = Long.parseLong(bundle.getString(Constants.BundleKeys.GROUP_ID));
        getGroupSummary(groupId.intValue());

    }

    @Override
    public void updateGroupMembers(){

        String myId = NostragamusDataHandler.getInstance().getUserId();
        List<GroupPerson> groupMembers = mGroupInfo.getMembers();

        for (GroupPerson groupPerson : groupMembers) {
            if (myId.compareTo(groupPerson.getId().toString()) == 0
                    && groupPerson.isAdmin()) {
                mAdmin = true;
                break;
            }
        }


    }

    @Override
    public void updateGroupPhoto(File file, String filepath, String filename) {

        if(filepath.equals(null)) {
            mGroupInfoModelListener.onGroupImagePathNull();
            return;
        }
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            mGroupInfoModelListener.onUpdating();
            callUpdateGroupPhotoApi(file,filepath,filename);
        } else {
            mGroupInfoModelListener.onNoInternet();
        }
    }


    private void callUpdateGroupPhotoApi(File file, String filepath, String filename) {

        MyWebService.getInstance().getUploadPhotoRequest(file,filepath,filename).enqueue(new NostragamusCallBack<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    mGroupInfo.setPhoto(response.body().getResult());
                    mGroupInfoModelListener.onPhotoUpdate(mGroupInfo.getPhoto());
                } else {
                    mGroupInfoModelListener.onEditFailed(response.message());
                }
            }

        });

    }

    private void getAllTournamentsfromServer() {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getCurrentTournaments(true).enqueue(
                    new NostragamusCallBack<TournamentFeedResponse>() {
                        @Override
                        public void onResponse(Call<TournamentFeedResponse> call, Response<TournamentFeedResponse> response) {
                            super.onResponse(call, response);
                            if(response.isSuccessful()) {
                                List<TournamentFeedInfo> newTournamentInfo = response.body().getTournamentInfos();

                                if(null != newTournamentInfo && newTournamentInfo.size() > 0) {
                                    List<TournamentFeedInfo> oldTournamentList = mNostragamusDataHandler.getTournaments();
                                    oldTournamentList.clear();
                                    for (TournamentFeedInfo tournamentInfo : newTournamentInfo) {
                                        if(!oldTournamentList.contains(tournamentInfo)) {
                                            oldTournamentList.add(tournamentInfo);
                                        }
                                    }

                                    mNostragamusDataHandler.setTournaments(oldTournamentList);
                                    mGrpTournamentSelectionAdapter.addAll(NostragamusDataHandler.getInstance().getTournaments());

                                    mGroupInfoModelListener.onSuccessTournamentInfo();
                                }

                            } else {
                                mGroupInfoModelListener.onFailed(response.message());
                            }
                        }
                    }
            );
        } else {
            mGroupInfoModelListener.onNoInternet();
        }
    }

    @Override
    public void updateTournaments(){

        Log.i("inside","updateTournaments");

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

    private void getGroupSummary(Integer GroupId) {
        MyWebService.getInstance().getGroupSummaryRequest(GroupId).enqueue(
                new NostragamusCallBack<GroupSummaryResponse>() {
                    @Override
                    public void onResponse(Call<GroupSummaryResponse> call, Response<GroupSummaryResponse> response) {
                        super.onResponse(call, response);
                        if(response.isSuccessful()) {

                            GroupInfo groupInfo = response.body().getGroupInfo();

                            groupInfo.setMembers(groupInfo.getMembers());


                            mGroupInfo = groupInfo;

                            mGroupInfoModelListener.onGetGroupSummarySuccess(groupInfo);
                        } else {
                            Log.i("inside","responsefailed");
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
    public GrpTournamentSelectionAdapter getAdapter(Context context) {

        getAllTournamentsfromServer();
        List<TournamentFeedInfo> followedTournaments = mGroupInfo.getFollowedTournaments();

        List<Integer> mFollowedTournamentsIdList = new ArrayList<>();
        for (TournamentFeedInfo tournamentInfo : followedTournaments) {
            mFollowedTournamentsIdList.add(tournamentInfo.getTournamentId());
        }


        List<TournamentFeedInfo> unfollowedTournaments = mNostragamusDataHandler.getTournaments();
        unfollowedTournaments.removeAll(followedTournaments);

        List<Integer> mUnFollowedTournamentsIdList = new ArrayList<>();
        for (TournamentFeedInfo tournamentInfo : unfollowedTournaments) {
            mUnFollowedTournamentsIdList.add(tournamentInfo.getTournamentId());
        }



        this.mGrpTournamentSelectionAdapter = new GrpTournamentSelectionAdapter(context,
                mFollowedTournamentsIdList, mUnFollowedTournamentsIdList,new GrpTournamentSelectionAdapter.OnGrpTournamentChangedListener() {

            @Override
            public boolean onGrpTournamentSelected(boolean addNewTournament, int existingTournamentCount) {
                return mAdmin && (addNewTournament || existingTournamentCount > 1);
            }

            @Override
            public void onGrpTournamentChanged(List<Integer> selectedTournamentsIdList) {
                mGrpTournamentUpdateModel.updateGrpTournaments(selectedTournamentsIdList);
            }

            @Override
            public void removeSelectedTournament(int adapterPosition) {

            }

            @Override
            public void addSelectedTournament(int adapterPosition) {

            }

        });

//        if(amAdmin()) {
//            this.mGrpTournamentSelectionAdapter.addAll(NostragamusDataHandler.getInstance().getTournaments());
//        } else {
//            this.mGrpTournamentSelectionAdapter.addAll(followedTournaments);
//        }
        return mGrpTournamentSelectionAdapter;
    }

    @Override
    public Bundle getGroupIdBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.BundleKeys.GROUP_ID, mGroupInfo.getId());
        return bundle;
    }

    @Override
    public void updateGroupName(String groupName,String photo) {
        if (groupName.isEmpty()) {
            mGroupInfoModelListener.onGroupNameEmpty();
            return;
        }

        mGrpNameUpdateModel.updateGrpName(groupName,photo);
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

    public interface OnEditGroupInfoModelListener {

        void onGroupNameUpdateSuccess();

        void onGroupNameEmpty();

        void onNoInternet();

        void onGroupTournamentUpdateSuccess();

        void onLeaveGroupSuccess();

        void onFailed(String message);

        void onGetGroupSummarySuccess(GroupInfo grpInfoList);

        void onGetGroupSummaryFailed(String message);

        Context getContext();

        void onGroupImagePathNull();

        void onUpdating();

        void onPhotoUpdate(String photo);

        void onEditFailed(String message);

        void onSuccessTournamentInfo();
    }
}