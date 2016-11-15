package in.sportscafe.scgame.module.feed;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.module.feed.dto.Feed;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.feed.dto.MatchesResponse;
import in.sportscafe.scgame.module.tournamentFeed.dto.Tournament;
import in.sportscafe.scgame.utils.timeutils.TimeUtils;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedModelImpl implements FeedModel {

    private int mClosestDatePosition = 0;

    private FeedAdapter mFeedAdapter;

    private OnFeedModelListener mFeedModelListener;

    private Integer tourId;

    private FeedModelImpl(OnFeedModelListener listener) {
        this.mFeedModelListener = listener;
    }

    public static FeedModel newInstance(OnFeedModelListener listener) {
        return new FeedModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {

         tourId = bundle.getInt(Constants.BundleKeys.TOURNAMENT_ID);
    }

    @Override
    public FeedAdapter getAdapter() {
        return mFeedAdapter = new FeedAdapter(mFeedModelListener.getContext());
    }

    @Override
    public void getFeeds() {
        mFeedAdapter.clear();
        if(ScGame.getInstance().hasNetworkConnection()) {
            callFeedListApi(tourId);
        } else {
            mFeedModelListener.onNoInternet();
        }
    }

    private void callFeedListApi(Integer tourId) {
        MyWebService.getInstance().getMatches(tourId).enqueue(new ScGameCallBack<MatchesResponse>() {
            @Override
            public void onResponse(Call<MatchesResponse> call, Response<MatchesResponse> response) {
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
        mFeedAdapter.addAll(getFeedList(matchList));
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
                return (int) (lhs.getDate() - rhs.getDate());
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

        void onSuccessFeeds(FeedAdapter feedAdapter, int movePosition);

        void onFailedFeeds(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();
    }
}