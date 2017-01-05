package in.sportscafe.nostragamus.module.play.myresultstimeline;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
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

public class MyResultsTimelineModelImpl implements MyResultsTimelineModel {

    private int mClosestDatePosition = 0;

    private MyResultsTimelineAdapter mMyResultsTimelineAdapter;

    private MyResultsTimelineModelImpl.OnMyResultsTimelineModelListener mMyResultsTimelineModelListener;

    private Integer tourId;

    private MyResultsTimelineModelImpl(MyResultsTimelineModelImpl.OnMyResultsTimelineModelListener listener) {
        this.mMyResultsTimelineModelListener = listener;
    }

    public static MyResultsTimelineModel newInstance(MyResultsTimelineModelImpl.OnMyResultsTimelineModelListener listener) {
        return new MyResultsTimelineModelImpl(listener);
    }

    @Override
    public MyResultsTimelineAdapter getAdapter() {
        return mMyResultsTimelineAdapter = new MyResultsTimelineAdapter(mMyResultsTimelineModelListener.getContext());
    }

    @Override
    public void getFeeds() {
        mMyResultsTimelineAdapter.clear();
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callFeedListApi();
        } else {
            mMyResultsTimelineModelListener.onNoInternet();
        }
    }

    private void callFeedListApi() {
        MyWebService.getInstance().getMatchResults(true,true).enqueue(new NostragamusCallBack<MatchesResponse>() {
            @Override
            public void onResponse(Call<MatchesResponse> call, Response<MatchesResponse> response) {
                super.onResponse(call, response);
                if(null == mMyResultsTimelineModelListener.getContext()) {
                    return;
                }

                if(response.isSuccessful()) {
                    List<Match> matchList = response.body().getMatches();

                    if(null == matchList || matchList.isEmpty()) {
                        mMyResultsTimelineModelListener.onEmpty();
                        return;
                    }

                    handleMatches(matchList);
                } else {

                    mMyResultsTimelineModelListener.onFailedFeeds(response.message());
                }
            }
        });
    }

    private void handleMatches(List<Match> matchList) {
        mMyResultsTimelineAdapter.addAll(getFeedList(matchList));
        mMyResultsTimelineModelListener.onSuccessFeeds(mMyResultsTimelineAdapter, mClosestDatePosition);
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


    public interface OnMyResultsTimelineModelListener {

        void onSuccessFeeds(MyResultsTimelineAdapter myResultsTimelineAdapter, int movePosition);

        void onFailedFeeds(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();
    }
}
