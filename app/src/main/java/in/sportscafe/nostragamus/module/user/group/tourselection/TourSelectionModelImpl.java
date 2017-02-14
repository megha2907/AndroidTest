package in.sportscafe.nostragamus.module.user.group.tourselection;

import android.content.Context;
import android.os.Bundle;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedResponse;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GrpTournamentUpdateModelImpl;
import in.sportscafe.nostragamus.module.user.group.newgroup.TourSelectionAdapter;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 1/6/17.
 */
public class TourSelectionModelImpl implements TourSelectionModel {

    private boolean mAdmin = false;

    private int mGroupId;

    private List<TournamentFeedInfo> mFollowedTours;

    private TourSelectionAdapter mTourSelectionAdapter;

    private OnGroupSelectionModelListener mGroupInfoModelListener;

    private GrpTournamentUpdateModelImpl mGrpTournamentUpdateModel;

    private TourSelectionModelImpl(OnGroupSelectionModelListener listener) {
        this.mGroupInfoModelListener = listener;
    }

    public static TourSelectionModel newInstance(OnGroupSelectionModelListener listener) {
        return new TourSelectionModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mAdmin = bundle.getBoolean(BundleKeys.IS_ADMIN);
        mGroupId = bundle.getInt(BundleKeys.GROUP_ID);
        mFollowedTours = Parcels.unwrap(bundle.getParcelable(BundleKeys.FOLLOWED_TOURS));

        this.mGrpTournamentUpdateModel = new GrpTournamentUpdateModelImpl(mGroupId,
                new GrpTournamentUpdateModelImpl.OnGrpTournamentUpdateModelListener() {
                    @Override
                    public void onSuccessGrpTournamentUpdate() {
                        mGroupInfoModelListener.onTourUpdateSuccess(mTourSelectionAdapter.getSelectedTourList());
                    }

                    @Override
                    public void onFailedGrpTournamentUpdate(String message) {
                        mGroupInfoModelListener.onTourUpdateFailed();
                    }

                    @Override
                    public void onNoInternet() {
                        mGroupInfoModelListener.onNoInternet();
                    }
                });
    }

    @Override
    public void getAllTournaments() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getCurrentTournaments(true).enqueue(
                    new NostragamusCallBack<TournamentFeedResponse>() {
                        @Override
                        public void onResponse(Call<TournamentFeedResponse> call, Response<TournamentFeedResponse> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful()) {
                                mGroupInfoModelListener.onGetAllToursSuccess(response.body().getTournamentInfos());
                            } else {
                                mGroupInfoModelListener.onGetAllToursFailed(response.message());
                            }
                        }
                    }
            );
        } else {
            mGroupInfoModelListener.onNoInternet();
        }
    }

    @Override
    public TourSelectionAdapter getTourSelectionAdapter(Context context, List<TournamentFeedInfo> allTours) {
        List<Integer> followedTourIdList = new ArrayList<>();
        for (TournamentFeedInfo tourInfo : mFollowedTours) {
            followedTourIdList.add(tourInfo.getTournamentId());
            allTours.remove(tourInfo);
        }

        this.mTourSelectionAdapter = new TourSelectionAdapter(context, mFollowedTours, followedTourIdList,
                new TourSelectionAdapter.OnGrpTournamentChangedListener() {
                    @Override
                    public void onGrpTournamentClicked(int position, boolean selected) {
                        if (mAdmin) {
                            if (mTourSelectionAdapter.getSelectedTourIdList().size() == 1 && selected) {
                                mGroupInfoModelListener.selectedTournamentsLimit();
                                return;
                            }

                            TournamentFeedInfo feedInfo = mTourSelectionAdapter.getItem(position);
                            mGroupInfoModelListener.onUpdatingTourSelection();
                            mTourSelectionAdapter.updateSelectionList(feedInfo);
                            mTourSelectionAdapter.remove(position);

                            if (selected) {
                                mTourSelectionAdapter.add(feedInfo);
                            } else {
                                mTourSelectionAdapter.add(feedInfo, 0);
                            }

                            mTourSelectionAdapter.notifyItemChanged(position);
                            mGrpTournamentUpdateModel.updateGrpTournaments(mTourSelectionAdapter.getSelectedTourIdList());
                        } else {
                            mGroupInfoModelListener.requireAdminAccess();
                        }
                    }
                });
        // mTourSelectionAdapter.add(selectedLabel);
        mTourSelectionAdapter.addAll(mFollowedTours);
        mTourSelectionAdapter.add(new TournamentFeedInfo(-2));
        mTourSelectionAdapter.addAll(allTours);
        return mTourSelectionAdapter;
    }

    public interface OnGroupSelectionModelListener {

        void onNoInternet();

        void onGetAllToursSuccess(List<TournamentFeedInfo> allTours);

        void onGetAllToursFailed(String message);

        void selectedTournamentsLimit();

        void requireAdminAccess();

        void onUpdatingTourSelection();

        void onTourUpdateSuccess(List<TournamentFeedInfo> followedTours);

        void onTourUpdateFailed();
    }
}