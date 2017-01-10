package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    private GrpTournamentSelectionAdapter mGrpTournamentSelectedAdapter;

    private GrpTournamentSelectionAdapter mGrpTournamentUnSelectedAdapter;

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
    public void getAllTournamentsfromServer() {

        if(Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getCurrentTournaments(true).enqueue(
                    new NostragamusCallBack<TournamentFeedResponse>() {
                        @Override
                        public void onResponse(Call<TournamentFeedResponse> call, Response<TournamentFeedResponse> response) {
                            super.onResponse(call, response);
                            if(response.isSuccessful()) {
                                mGroupInfoModelListener.onSuccessTournamentInfo(response.body().getTournamentInfos());
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
    public GrpTournamentSelectionAdapter getSelectedAdapter(Context context,List<TournamentFeedInfo> tournamentInfos) {
        List<TournamentFeedInfo> followedTournaments = mGroupInfo.getFollowedTournaments();

        List<Integer> mFollowedTournamentsIdList = new ArrayList<>();
        for (TournamentFeedInfo tournamentInfo : followedTournaments) {
            mFollowedTournamentsIdList.add(tournamentInfo.getTournamentId());
        }


        for (TournamentFeedInfo tournamentInfo : followedTournaments) {
            tournamentInfos.remove(tournamentInfo);
        }

//        TournamentFeedInfo selectedLabel = new TournamentFeedInfo();
//        selectedLabel.setTournamentId(-1);

        TournamentFeedInfo unselectedLabel = new TournamentFeedInfo();
        unselectedLabel.setTournamentId(-2);

        this.mGrpTournamentSelectedAdapter = new GrpTournamentSelectionAdapter(context, mFollowedTournamentsIdList,
                new GrpTournamentSelectionAdapter.OnGrpTournamentChangedListener() {
                    @Override
                    public void onGrpTournamentClicked(int position, boolean selected) {
                        TournamentFeedInfo feedInfo = mGrpTournamentSelectedAdapter.getItem(position);

                        mGrpTournamentSelectedAdapter.updateSelectionList(feedInfo);
                        mGrpTournamentSelectedAdapter.remove(position);

                        if(selected) {
                            mGrpTournamentSelectedAdapter.add(feedInfo);
                        } else {
                            mGrpTournamentSelectedAdapter.add(feedInfo, 1);
                        }
                        mGrpTournamentSelectedAdapter.notifyItemChanged(position);
                        mGroupInfoModelListener.setTournamentsCount(mGrpTournamentSelectedAdapter.getSelectedTournamentList().size());


                    }
                });
       // mGrpTournamentSelectedAdapter.add(selectedLabel);
        mGrpTournamentSelectedAdapter.addAll(followedTournaments);
        mGrpTournamentSelectedAdapter.add(unselectedLabel);
        mGrpTournamentSelectedAdapter.addAll(tournamentInfos);

        mGroupInfoModelListener.setTournamentsCount(mGrpTournamentSelectedAdapter.getSelectedTournamentList().size());

//        if(amAdmin()) {
//            this.mGrpTournamentSelectionAdapter.addAll(NostragamusDataHandler.getInstance().getTournaments());
//        } else {
//            this.mGrpTournamentSelectionAdapter.addAll(followedTournaments);
//        }
        return mGrpTournamentSelectedAdapter;
    }

    @Override
    public GrpTournamentSelectionAdapter getUnSelectedAdapter(Context context, List<TournamentFeedInfo> tournamentInfos) {

        for (TournamentFeedInfo tournamentInfo : mGroupInfo.getFollowedTournaments()) {
            tournamentInfos.remove(tournamentInfo);
        }

        this.mGrpTournamentUnSelectedAdapter = new GrpTournamentSelectionAdapter(context, new ArrayList<Integer>(),
                new GrpTournamentSelectionAdapter.OnGrpTournamentChangedListener() {
                    @Override
                    public void onGrpTournamentClicked(int position, boolean selected) {
                        TournamentFeedInfo feedInfo = mGrpTournamentUnSelectedAdapter.getItem(position);
                        mGrpTournamentUnSelectedAdapter.remove(position);
                        mGrpTournamentUnSelectedAdapter.notifyItemRemoved(position);


                        mGrpTournamentSelectedAdapter.updateSelectionList(feedInfo);
                        mGrpTournamentSelectedAdapter.add(feedInfo);
                        mGrpTournamentSelectedAdapter.notifyDataSetChanged();
                    }
                });
        this.mGrpTournamentUnSelectedAdapter.addAll(tournamentInfos);

        return mGrpTournamentUnSelectedAdapter;
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

        void onSuccessTournamentInfo(List<TournamentFeedInfo> tournamentInfos);

        void setTournamentsCount(int size);
    }
}
