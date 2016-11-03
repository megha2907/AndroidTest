package in.sportscafe.scgame.module.user.group.newgroup;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentsResponse;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.Result;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
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

    private ScGameDataHandler mScGameDataHandler;

    private String mgroupPhoto;

    private NewGroupModelImpl(OnNewGroupModelListener listener) {
        this.mNewGroupModelListener = listener;
        mScGameDataHandler = ScGameDataHandler.getInstance();
    }

    public static NewGroupModel newInstance(OnNewGroupModelListener listener) {
        return new NewGroupModelImpl(listener);
    }

    @Override
    public GrpTournamentSelectionAdapter getAdapter(Context context) {

        getAllTournamentsfromServer();

        this.mTournamentSelectionAdapter = new GrpTournamentSelectionAdapter(context, new ArrayList<Integer>());
        this.mTournamentSelectionAdapter.addAll(ScGameDataHandler.getInstance().getTournaments());
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
            if(ScGame.getInstance().hasNetworkConnection()) {
                NewGroupRequest newGroupRequest = new NewGroupRequest();
                newGroupRequest.setGroupCreatedBy(ScGameDataHandler.getInstance().getUserId());
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
        if(ScGame.getInstance().hasNetworkConnection()) {
            mNewGroupModelListener.onUpdating();
            callUpdateGroupPhotoApi(file,filepath,filename);
        } else {
            mNewGroupModelListener.onNoInternet();
        }
    }


    private void callUpdateGroupPhotoApi(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {

        MyWebService.getInstance().getUpdateGroupPhotoRequest(file,filepath,filename).enqueue(new ScGameCallBack<Result>() {
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
        if(ScGame.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getTournaments(true).enqueue(
                    new ScGameCallBack<TournamentsResponse>() {
                        @Override
                        public void onResponse(Call<TournamentsResponse> call, Response<TournamentsResponse> response) {
                            if(response.isSuccessful()) {
                                List<TournamentInfo> newTournamentInfo = response.body().getTournamentInfos();

                                if(null != newTournamentInfo && newTournamentInfo.size() > 0) {
                                    List<TournamentInfo> oldTournamentList = mScGameDataHandler.getTournaments();
                                    oldTournamentList.clear();
                                    for (TournamentInfo tournamentInfo : newTournamentInfo) {
                                        if(!oldTournamentList.contains(tournamentInfo)) {
                                            oldTournamentList.add(tournamentInfo);
                                        }
                                    }

                                    mScGameDataHandler.setTournaments(oldTournamentList);

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
                new ScGameCallBack<NewGroupResponse>() {
                    @Override
                    public void onResponse(Call<NewGroupResponse> call, Response<NewGroupResponse> response) {
                        if(response.isSuccessful()) {
                            ScGameDataHandler scGameDataHandler = ScGameDataHandler.getInstance();

                            GroupInfo groupInfo = response.body().getGroupInfo();

                            Map<Long, GroupInfo> grpInfoMap = scGameDataHandler.getGrpInfoMap();
                            grpInfoMap.put(groupInfo.getId(), groupInfo);

                            scGameDataHandler.setGrpInfoMap(grpInfoMap);

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
        values.put("UserID", ScGameDataHandler.getInstance().getUserId());

        ScGameAnalytics.getInstance().trackOtherEvents("CREATE GROUP-ONCLICK", values);
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