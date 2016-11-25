package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupModelImpl implements NewGroupModel {

    private GrpTournamentSelectionAdapter mTournamentSelectionAdapter;

    private OnNewGroupModelListener mNewGroupModelListener;

    private NostragamusDataHandler mNostragamusDataHandler;

    private String mgroupPhoto;

    private NewGroupModelImpl(OnNewGroupModelListener listener) {
        this.mNewGroupModelListener = listener;
        mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static NewGroupModel newInstance(OnNewGroupModelListener listener) {
        return new NewGroupModelImpl(listener);
    }

    @Override
    public GrpTournamentSelectionAdapter getAdapter(Context context) {

        getAllTournamentsfromServer();

        this.mTournamentSelectionAdapter = new GrpTournamentSelectionAdapter(context, new ArrayList<Integer>());
        return mTournamentSelectionAdapter;
    }

    @Override
    public void createGroup(String groupName) {
        if(groupName.isEmpty()) {
            mNewGroupModelListener.onEmptyGroupName();
            return;
        }

        List<Integer> SelectedTournament = mTournamentSelectionAdapter.getSelectedTournamentList();
        if(null == SelectedTournament || SelectedTournament.isEmpty()) {
            mNewGroupModelListener.onNoSportSelected();
            return;
        } else {
            if(Nostragamus.getInstance().hasNetworkConnection()) {
                NewGroupRequest newGroupRequest = new NewGroupRequest();
                newGroupRequest.setGroupCreatedBy(NostragamusDataHandler.getInstance().getUserId());
                newGroupRequest.setGroupName(groupName);
                newGroupRequest.setfollowedTournaments(SelectedTournament);
                newGroupRequest.setGroupPhoto(mgroupPhoto);
                callNewGroupApi(newGroupRequest);
            } else {
                mNewGroupModelListener.onNoInternet();
            }
        }
    }

    @Override
    public void updateGroupPhoto(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {

        if(filepath.equals(null)) {
            mNewGroupModelListener.onGroupImagePathNull();
            return;
        }
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            mNewGroupModelListener.onUpdating();
            callUpdateGroupPhotoApi(file,filepath,filename);
        } else {
            mNewGroupModelListener.onNoInternet();
        }
    }


    private void callUpdateGroupPhotoApi(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {

        MyWebService.getInstance().getUpdateGroupPhotoRequest(file,filepath,filename).enqueue(new NostragamusCallBack<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.isSuccessful()) {
                    mgroupPhoto = response.body().getResult();
                    mNewGroupModelListener.onPhotoUpdate(mgroupPhoto);
                } else {
                    mNewGroupModelListener.onEditFailed(response.message());
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
                                    mTournamentSelectionAdapter.addAll(NostragamusDataHandler.getInstance().getTournaments());

                                    mNewGroupModelListener.onSuccessTournamentInfo();
                                }

                            } else {
                                mNewGroupModelListener.onFailed(response.message());
                            }
                        }
                    }
            );
        } else {
            mNewGroupModelListener.onNoInternet();
        }
    }


    private void callNewGroupApi(NewGroupRequest newGroupRequest) {
        MyWebService.getInstance().getNewGroupRequest(newGroupRequest).enqueue(
                new NostragamusCallBack<NewGroupResponse>() {
                    @Override
                    public void onResponse(Call<NewGroupResponse> call, Response<NewGroupResponse> response) {
                        if(response.isSuccessful()) {
                            NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();

                            GroupInfo groupInfo = response.body().getGroupInfo();

                            Map<Long, GroupInfo> grpInfoMap = nostragamusDataHandler.getGrpInfoMap();
                            grpInfoMap.put(groupInfo.getId(), groupInfo);

                            nostragamusDataHandler.setGrpInfoMap(grpInfoMap);

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BundleKeys.GROUP_ID, String.valueOf(groupInfo.getId()));
                            bundle.putString(Constants.BundleKeys.GROUP_NAME, groupInfo.getName());
                            mNewGroupModelListener.onSuccess(bundle);
                        } else {
                            mNewGroupModelListener.onFailed(response.message());
                        }
                    }
                }
        );

        Map<String, String> values = new HashMap<>();
        values.put("GroupName", newGroupRequest.getGroupName());
        values.put("UserID", NostragamusDataHandler.getInstance().getUserId());

        NostragamusAnalytics.getInstance().trackOtherEvents("CREATE GROUP-ONCLICK", values);
    }

    public interface OnNewGroupModelListener {

        void onSuccess(Bundle bundle);

        void onSuccessTournamentInfo();

        void onEmptyGroupName();

        void onNoSportSelected();

        void onNoInternet();

        void onFailed(String message);

        void onGroupImagePathNull();

        void onUpdating();

        void onEditFailed(String message);

        void onPhotoUpdate(String groupPhoto);
    }
}