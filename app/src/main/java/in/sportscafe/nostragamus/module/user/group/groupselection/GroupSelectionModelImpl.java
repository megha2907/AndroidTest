package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GrpNameUpdateModelImpl;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GrpTournamentUpdateModelImpl;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.webservice.GroupSummaryResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 1/6/17.
 */

public class GroupSelectionModelImpl implements GroupSelectionModel {

    private boolean mAdmin = false;

    private GroupInfo mGroupInfo;

    private GrpTournamentSelectionAdapter mGrpTournamentSelectionAdapter;

    private OnGroupSelectionModelListener mGroupInfoModelListener;

    private GrpTournamentUpdateModelImpl mGrpTournamentUpdateModel;

    private GrpNameUpdateModelImpl mGrpNameUpdateModel;

    private NostragamusDataHandler mNostragamusDataHandler;

    private GroupSelectionModelImpl(OnGroupSelectionModelListener listener) {
        this.mGroupInfoModelListener = listener;
        mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static GroupSelectionModel newInstance(OnGroupSelectionModelListener listener) {
        return new GroupSelectionModelImpl(listener);
    }

    @Override
    public void init(long groupId) {

        getGroupSummary((int)(long) groupId);

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
    public boolean amAdmin() {
        return mAdmin;
    }


    @Override
    public void updateTournaments(){

        com.jeeva.android.Log.i("inside","updateTournamentssele");

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
    public GroupInfo getGroupInfo() {
        return mGroupInfo;
    }

    @Override
    public GrpTournamentSelectionAdapter getAdapter(Context context) {

        getAllTournamentsfromServer();
        List<TournamentFeedInfo> followedTournaments = mGroupInfo.getFollowedTournaments();

        List<Integer> mFollowedTournamentsIdList = new ArrayList<>();
        for (TournamentFeedInfo tournamentInfo : followedTournaments) {
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
            this.mGrpTournamentSelectionAdapter.addAll(NostragamusDataHandler.getInstance().getTournaments());
        } else {
            this.mGrpTournamentSelectionAdapter.addAll(followedTournaments);
        }
        return mGrpTournamentSelectionAdapter;
    }



    @Override
    public void refreshGroupInfo() {
        this.mGroupInfo = NostragamusDataHandler.getInstance().getGrpInfoMap().get(mGroupInfo.getId());
    }


    public interface OnGroupSelectionModelListener {

        void onNoInternet();

        void onGroupTournamentUpdateSuccess();

        void onFailed(String message);

        void onGetGroupSummarySuccess(GroupInfo grpInfoList);

        void onGetGroupSummaryFailed(String message);

        Context getContext();

        void onSuccessTournamentInfo();
    }
}
