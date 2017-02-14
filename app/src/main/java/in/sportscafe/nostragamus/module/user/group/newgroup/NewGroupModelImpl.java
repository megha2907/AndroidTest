package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupModelImpl implements NewGroupModel {

    private TourSelectionAdapter mTournamentSelectionAdapter;

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
    public TourSelectionAdapter getAdapter(Context context, List<TournamentFeedInfo> tourList) {
        this.mTournamentSelectionAdapter = new TourSelectionAdapter(context, new ArrayList<TournamentFeedInfo>(), new ArrayList<Integer>(),
                new TourSelectionAdapter.OnGrpTournamentChangedListener() {
                    @Override
                    public void onGrpTournamentClicked(int position, boolean selected) {

                        TournamentFeedInfo feedInfo = mTournamentSelectionAdapter.getItem(position);

                        if (mTournamentSelectionAdapter.getSelectedTourIdList().size() == 1 && selected) {
                            mNewGroupModelListener.selectedTournamentsLimit();
                            return;

                        } else {

                            mTournamentSelectionAdapter.updateSelectionList(feedInfo);
                            mTournamentSelectionAdapter.remove(position);

                            if (selected) {
                                mTournamentSelectionAdapter.add(feedInfo);
                            } else {
                                mTournamentSelectionAdapter.add(feedInfo, 0);
                            }

                            mTournamentSelectionAdapter.notifyItemChanged(position);
                        }

                    }
                });

        mTournamentSelectionAdapter.add(new TournamentFeedInfo(-2));
        mTournamentSelectionAdapter.addAll(tourList);

        return mTournamentSelectionAdapter;
    }

    @Override
    public void createGroup(String groupName) {
        if (groupName.isEmpty()) {
            mNewGroupModelListener.onEmptyGroupName();
            return;
        }

        List<Integer> selectedTournamentList = mTournamentSelectionAdapter.getSelectedTourIdList();
        if (null == selectedTournamentList || selectedTournamentList.isEmpty()) {
            mNewGroupModelListener.onNoTournamentSelected();
            return;
        } else {
            mNewGroupModelListener.setGroupDoneBtnClickable(false);
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                NewGroupRequest newGroupRequest = new NewGroupRequest();
                newGroupRequest.setGroupCreatedBy(NostragamusDataHandler.getInstance().getUserId());
                newGroupRequest.setGroupName(groupName);
                newGroupRequest.setfollowedTournaments(selectedTournamentList);
                newGroupRequest.setGroupPhoto(mgroupPhoto);
                callNewGroupApi(newGroupRequest);
            } else {
                mNewGroupModelListener.onNoInternet();
            }
        }
    }

    @Override
    public void updateGroupPhoto(File file, String filepath, String filename) {

        if (filepath.equals(null)) {
            mNewGroupModelListener.onGroupImagePathNull();
            return;
        }
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            mNewGroupModelListener.onUpdating();
            callUpdateGroupPhotoApi(file, filepath, UUID.randomUUID().toString() + "_" + filename);
        } else {
            mNewGroupModelListener.onNoInternet();
        }
    }

    @Override
    public void onGetImage(Intent data) {
        String imagePath = data.getStringExtra(BundleKeys.ADDED_NEW_IMAGE_PATH);
        Log.i("file", imagePath);

        File file = new File(imagePath);
        updateGroupPhoto(file, "game/groupimages/", file.getName());
    }


    private void callUpdateGroupPhotoApi(File file, String filepath, String filename) {

        MyWebService.getInstance().getUploadPhotoRequest(file, filepath, filename).enqueue(new NostragamusCallBack<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    mgroupPhoto = response.body().getResult();
                    mNewGroupModelListener.onPhotoUpdate(mgroupPhoto);
                } else {
                    mNewGroupModelListener.onEditFailed(response.message());
                }
            }

        });

    }

    @Override
    public void getAllTournamentsfromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getCurrentTournaments(true).enqueue(
                    new NostragamusCallBack<TournamentFeedResponse>() {
                        @Override
                        public void onResponse(Call<TournamentFeedResponse> call, Response<TournamentFeedResponse> response) {
                            super.onResponse(call, response);

                            if (response.isSuccessful()) {
                                List<TournamentFeedInfo> tourList = response.body().getTournamentInfos();
                                if (null != tourList && tourList.size() > 0) {
                                    mNewGroupModelListener.onSuccessTournamentInfo(tourList);
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


    private void callNewGroupApi(final NewGroupRequest newGroupRequest) {
        MyWebService.getInstance().getNewGroupRequest(newGroupRequest).enqueue(
                new NostragamusCallBack<NewGroupResponse>() {
                    @Override
                    public void onResponse(Call<NewGroupResponse> call, Response<NewGroupResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            NostragamusAnalytics.getInstance().trackGroups(
                                    AnalyticsActions.NEW_GROUP,
                                    MyWebService.getInstance().getJsonStringFromObject(newGroupRequest.getfollowedTournaments())
                            );

                            GroupInfo groupInfo = response.body().getGroupInfo();

                            Bundle bundle = new Bundle();
                            bundle.putInt(BundleKeys.GROUP_ID, groupInfo.getId());
                            bundle.putString(BundleKeys.GROUP_NAME, groupInfo.getName());
                            bundle.putString(BundleKeys.SCREEN, ScreenNames.GROUPS_CREATE_NEW);

                            mNewGroupModelListener.onSuccess(bundle);
                        } else {
                            mNewGroupModelListener.setGroupDoneBtnClickable(true);
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

        void onSuccessTournamentInfo(List<TournamentFeedInfo> tourList);

        void onEmptyGroupName();

        void onNoTournamentSelected();

        void onNoInternet();

        void onFailed(String message);

        void onGroupImagePathNull();

        void onUpdating();

        void onEditFailed(String message);

        void onPhotoUpdate(String groupPhoto);

        void selectedTournamentsLimit();

        void setGroupDoneBtnClickable(boolean btnClickable);
    }
}