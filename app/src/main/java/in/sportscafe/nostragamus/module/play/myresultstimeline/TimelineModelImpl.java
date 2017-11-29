package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.dto.MatchesResponse;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 10/5/16.
 */

public class TimelineModelImpl implements TimelineModel {

    private static final int PAGINATION_START_AT = 5;

    private static final int DEFAULT_LIMIT = 10;

    private Integer mPlayerUserId;

    private String mPlayerPhoto;

    private String mPlayerName;

    private Integer mRoomId;

    private int mClosestDatePosition = 0;

    private boolean mTimelineLoading = false;

    private boolean mHasMoreItems = true;

    private TimelineAdapter mTimelineAdapter;

    private TimelineModelImpl.OnMyResultsTimelineModelListener mMyResultsTimelineModelListener;

    private TimelineModelImpl(TimelineModelImpl.OnMyResultsTimelineModelListener listener) {
        this.mMyResultsTimelineModelListener = listener;
    }

    public static TimelineModel newInstance(TimelineModelImpl.OnMyResultsTimelineModelListener listener) {
        return new TimelineModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if (null != bundle) {
            if (bundle.containsKey(BundleKeys.PLAYER_USER_ID)) {
                mPlayerUserId = bundle.getInt(BundleKeys.PLAYER_USER_ID);
                mPlayerName = bundle.getString(BundleKeys.PLAYER_NAME);
                mPlayerPhoto = bundle.getString(BundleKeys.PLAYER_PHOTO);
            }

            if (bundle.containsKey(BundleKeys.ROOM_ID)) {
                mRoomId = bundle.getInt(BundleKeys.ROOM_ID);

                /* setting room Id 0 for current user */
                if (mRoomId==0){
                    mRoomId = null;
                }
            }
        }
    }

    @Override
    public TimelineAdapter getAdapter(Context context) {
        if (null == mPlayerUserId) {
            return mTimelineAdapter = new TimelineAdapter(context);
        }
        return mTimelineAdapter = new TimelineAdapter(context,mRoomId, mPlayerUserId, mPlayerName, mPlayerPhoto);
    }

    @Override
    public void getFeeds() {
        mTimelineAdapter.clear();
        callFeedListApi(0, DEFAULT_LIMIT);
    }

    @Override
    public void checkPagination(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0 && !mTimelineLoading) {
            int lastVisibleItem = firstVisibleItem + visibleItemCount;

            if (mHasMoreItems
                    && lastVisibleItem >= (totalItemCount - PAGINATION_START_AT)) {
                callFeedListApi(totalItemCount, DEFAULT_LIMIT);
            } else if (!mHasMoreItems) {
                mMyResultsTimelineModelListener.onAllTimelinesFetched();
            }
        }
    }

    @Override
    public boolean isAdapterEmpty() {
        return null == mTimelineAdapter || mTimelineAdapter.getItemCount() == 0;
    }

    @Override
    public String getChallengeNameIfAvailable() {
        if (null != mRoomId && null != mTimelineAdapter && mTimelineAdapter.getItemCount() > 0) {
            return mTimelineAdapter.getItem(0).getChallengeName()+" - "+mTimelineAdapter.getItem(0).getConfigName();
        }
        return null;
    }

    @Override
    public void clearChallengeDetails() {
        mRoomId = null;
    }

    @Override
    public void destroyAll() {
        mTimelineAdapter.destroy();
    }

    private void callFeedListApi(int skip, int limit) {
        if (!Nostragamus.getInstance().hasNetworkConnection()) {
            mMyResultsTimelineModelListener.onNoInternet();
            return;
        }

        mTimelineLoading = true;

        MyWebService.getInstance().getTimelinesRequest(mRoomId, mPlayerUserId, skip, limit)
                .enqueue(new NostragamusCallBack<MatchesResponse>() {
                    @Override
                    public void onResponse(Call<MatchesResponse> call, Response<MatchesResponse> response) {
                        super.onResponse(call, response);

                        if (mMyResultsTimelineModelListener.isThreadAlive()) {
                            if (response.isSuccessful()) {
                                handleMatches(response.body().getMatches());
                            } else {
                                mMyResultsTimelineModelListener.onFailedFeeds(response.message());
                            }
                        }

                        mTimelineLoading = false;
                    }
                });
    }

    private void handleMatches(List<Match> matchList) {

        if (null == matchList || matchList.isEmpty()) {
            if (null==mPlayerUserId) {
                mMyResultsTimelineModelListener.onEmpty(true);
            }else {
                mMyResultsTimelineModelListener.onEmpty(false);
            }
            return;
        }

        if (matchList.size() < DEFAULT_LIMIT) {
            mHasMoreItems = false;
        }

        mTimelineAdapter.addAll(matchList);
        mMyResultsTimelineModelListener.onSuccessFeeds();
    }

    public interface OnMyResultsTimelineModelListener {

        void onSuccessFeeds();

        void onFailedFeeds(String message);

        void onNoInternet();

        void onEmpty(Boolean isMyProfile);

        boolean isThreadAlive();

        void onAllTimelinesFetched();
    }
}