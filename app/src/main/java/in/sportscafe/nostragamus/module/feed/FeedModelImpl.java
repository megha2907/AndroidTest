package in.sportscafe.nostragamus.module.feed;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;
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
 * Created by Jeeva on 15/6/16.
 */
public class FeedModelImpl implements FeedModel {

    private int mClosestDatePosition = 0;

    private TimelineAdapter mFeedAdapter;

    private OnFeedModelListener mFeedModelListener;

    private Integer tourId;

    private String sportName;

    private FeedModelImpl(OnFeedModelListener listener) {
        this.mFeedModelListener = listener;
    }

    public static FeedModel newInstance(OnFeedModelListener listener) {
        return new FeedModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {

         tourId = bundle.getInt(Constants.BundleKeys.TOURNAMENT_ID);
        sportName = bundle.getString(Constants.BundleKeys.SPORT_NAME);
    }

    @Override
    public TimelineAdapter getAdapter() {
        return mFeedAdapter = new TimelineAdapter(mFeedModelListener.getContext());
    }

    @Override
    public void getFeeds() {
        mFeedAdapter.clear();
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callFeedListApi(tourId);
        } else {
            mFeedModelListener.onNoInternet();
        }
    }

    private void callFeedListApi(Integer tourId) {
        MyWebService.getInstance().getMatches(tourId).enqueue(new NostragamusCallBack<MatchesResponse>() {
            @Override
            public void onResponse(Call<MatchesResponse> call, Response<MatchesResponse> response) {
                super.onResponse(call, response);

                if(null == mFeedModelListener.getContext()) {
                    return;
                }

                if(response.isSuccessful()) {
                    List<Match> matchList = response.body().getMatches();

                    if(null == matchList || matchList.isEmpty()) {
                        mFeedModelListener.onEmpty();
                        return;
                    }

                    handleMatches(matchList);
                } else {

                    mFeedModelListener.onFailedFeeds(response.message());
                }
            }
        });
    }

    private void handleMatches(List<Match> matchList) {
        List<Feed> feedList = getFeedList(matchList);
        for (Feed feed : feedList) {
//            mFeedAdapter.add(feed);
        }

        mFeedModelListener.onSuccessFeeds(mFeedAdapter, mClosestDatePosition);
    }

    private List<Feed> getFeedList(List<Match> matchList) {
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
                if(lhs.getDate() > rhs.getDate()) {
                    return -1;
                } else if(lhs.getDate() < rhs.getDate()) {
                    return 1;
                } else {
                    return 0;
                }
//                return (int) (lhs.getDate() - rhs.getDate());
            }
        });


        long todayDateMs = TimeUtils.getMillisecondsFromDateString(
                TimeUtils.getCurrentTime(Constants.DateFormats.DD_MM_YYYY, Constants.DateFormats.GMT),
                Constants.DateFormats.DD_MM_YYYY, Constants.DateFormats.GMT);
        long tempMs;
        long closestDateMs = 0;

        for (int i = feedList.size() - 1; i >= 0; i--) {
            tempMs = feedList.get(i).getDate();
            if(todayDateMs - tempMs == 0) {
                mClosestDatePosition = i;
                break;
            }

            if(closestDateMs == 0 || Math.abs(todayDateMs - tempMs) < Math.abs(todayDateMs - closestDateMs)) {
                closestDateMs = tempMs;
                mClosestDatePosition = i;
            }
        }

        return feedList;
    }

    public interface OnFeedModelListener {

        void onSuccessFeeds(TimelineAdapter feedAdapter, int movePosition);

        void onFailedFeeds(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();
    }
}