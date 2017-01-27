package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.MatchesResponse;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
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

    private String mPlayerUserId;

    private int mClosestDatePosition = 0;

    private boolean mTimelineLoading = false;

    private boolean mHasMoreItems = true;

    private TimelineAdapter mMyResultsTimelineAdapter;

    private TimelineModelImpl.OnMyResultsTimelineModelListener mMyResultsTimelineModelListener;

    private TimelineModelImpl(TimelineModelImpl.OnMyResultsTimelineModelListener listener) {
        this.mMyResultsTimelineModelListener = listener;
    }

    public static TimelineModel newInstance(TimelineModelImpl.OnMyResultsTimelineModelListener listener) {
        return new TimelineModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if(null != bundle && bundle.containsKey(BundleKeys.PLAYER_USER_ID)) {
            mPlayerUserId = bundle.getString(BundleKeys.PLAYER_USER_ID);
        }
    }

    @Override
    public TimelineAdapter getAdapter(Context context) {
        return mMyResultsTimelineAdapter = new TimelineAdapter(context);
    }

    @Override
    public void getFeeds() {
        mMyResultsTimelineAdapter.clear();
        callFeedListApi(0, DEFAULT_LIMIT);
    }

    @Override
    public void checkPagination(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0 && !mTimelineLoading) {
            int lastVisibleItem = firstVisibleItem + visibleItemCount;

            if (mHasMoreItems
                    && lastVisibleItem >= (totalItemCount - PAGINATION_START_AT)) {
                callFeedListApi(totalItemCount, DEFAULT_LIMIT);
            } else if(!mHasMoreItems) {
                mMyResultsTimelineModelListener.onAllTimelinesFetched();
            }
        }
    }

    @Override
    public boolean isAdapterEmpty() {
        return null == mMyResultsTimelineAdapter || mMyResultsTimelineAdapter.getItemCount() == 0;
    }

    @Override
    public void destroyAll() {
        mMyResultsTimelineAdapter.destroy();
    }

    private void callFeedListApi(int skip, int limit) {
        if (!Nostragamus.getInstance().hasNetworkConnection()) {
            mMyResultsTimelineModelListener.onNoInternet();
            return;
        }

        mTimelineLoading = true;

        MyWebService.getInstance().getTimelinesRequest(mPlayerUserId, skip, limit)
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
            mMyResultsTimelineModelListener.onEmpty();
            return;
        }

        if(matchList.size() < DEFAULT_LIMIT) {
            mHasMoreItems = false;
        }

        mMyResultsTimelineAdapter.addAll(matchList);
        mMyResultsTimelineModelListener.onSuccessFeeds();
    }

    /*private List<Feed> getFeedList(List<Match> matchList) {
        Map<String, Tournament> tourMap = new HashMap<>();
        Map<String, Feed> feedMap = new HashMap<>();
        List<Feed> feedList = new ArrayList<>();

        String date;
        Feed feed;
        int tourId;
        Tournament tour;
        for (Match match : matchList) {
            date = TimeUtils.getFormattedDateString(
                    match.getStartTime(),
                    Constants.DateFormats.DD_MM_YYYY,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );

            if (feedMap.containsKey(date)) {
                feed = feedMap.get(date);
            } else {
                feed = new Feed(TimeUtils.getMillisecondsFromDateString(
                        date,
                        Constants.DateFormats.DD_MM_YYYY,
                        Constants.DateFormats.GMT
                ));
                feedMap.put(date, feed);
                feedList.add(feed);
            }

            tourId = match.getTournamentId();

            if (tourMap.containsKey(date + tourId)) {
                tour = tourMap.get(date + tourId);
            } else {
                tour = new Tournament(tourId, match.getTournamentName());
                tourMap.put(date + tourId, tour);
                feed.addTournament(tour);
            }

            tour.addMatches(match);
        }

        Collections.sort(feedList, new Comparator<Feed>() {
            @Override
            public int compare(Feed lhs, Feed rhs) {
                return (int) (rhs.getDate() - lhs.getDate());
            }
        });


        long todayDateMs = TimeUtils.getMillisecondsFromDateString(
                TimeUtils.getCurrentTime(Constants.DateFormats.DD_MM_YYYY, Constants.DateFormats.GMT),
                Constants.DateFormats.DD_MM_YYYY, Constants.DateFormats.GMT);
        long tempMs;
        long closestDateMs = 0;

        for (int i = feedList.size() - 1; i >= 0; i--) {
            tempMs = feedList.get(i).getDate();
            if (todayDateMs - tempMs == 0) {
                mClosestDatePosition = i;
                break;
            }

            if (closestDateMs == 0 || Math.abs(todayDateMs - tempMs) < Math.abs(todayDateMs - closestDateMs)) {
                closestDateMs = tempMs;
                mClosestDatePosition = i;
            }
        }

        return feedList;
    }*/


    public interface OnMyResultsTimelineModelListener {

        void onSuccessFeeds();

        void onFailedFeeds(String message);

        void onNoInternet();

        void onEmpty();

        boolean isThreadAlive();

        void onAllTimelinesFetched();
    }
}