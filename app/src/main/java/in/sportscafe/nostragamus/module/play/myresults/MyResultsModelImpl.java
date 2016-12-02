package in.sportscafe.nostragamus.module.play.myresults;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsModelImpl implements MyResultsModel, MyResultsAdapter.OnMyResultsActionListener {

    private static final int PAGINATION_START_AT = 5;

    private static final int LIMIT = 5;

    private  int matchId;

    private boolean isLoading = false;

    private boolean hasMoreItems = true;

    private MyResultsAdapter mResultAdapter;

    private OnMyResultsModelListener mResultsModelListener;

    private MyResultsModelImpl(OnMyResultsModelListener listener) {
        this.mResultsModelListener = listener;
    }

    public static MyResultsModel newInstance(OnMyResultsModelListener listener) {
        return new MyResultsModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        
        if(null != bundle.getString(Constants.BundleKeys.MATCH_ID))
        {
            String match_id = bundle.getString(Constants.BundleKeys.MATCH_ID);
            matchId= Integer.parseInt(match_id);
        }
        else
        {
            mResultsModelListener.onFailedMyResults(Constants.Alerts.RESULTS_INFO_ERROR);
            mResultsModelListener.gotoResultsTimeline();
        }
    }

    @Override
    public void getMyResultsData(Context context) {
        loadMyResults(0);
    }

    private void loadMyResults(int offset) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            Log.i("call","callMyResultsApi");
            callMyResultsApi(offset);
        } else {
            mResultsModelListener.onNoInternet();
        }
    }

    private void callMyResultsApi(final int offset) {
        isLoading = true;
        MyWebService.getInstance().getMyResultsRequest(matchId).enqueue(
                new NostragamusCallBack<MyResultsResponse>() {
                    @Override
                    public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            Context context = mResultsModelListener.getContext();
                            if (null != context) {
                                List<Match> myResultList = response.body().getMyResults();

                                String responseJson = MyWebService.getInstance().getJsonStringFromObject(myResultList);
                                Log.d("MyResults Response", responseJson);

                                if (offset == 0) {
                                    destroyAdapter();
                                    mResultsModelListener.onSuccessMyResults(createAdapter(context));

                                    loadAdapterData(getCategorizedList(myResultList));

                                    if (myResultList.isEmpty()) {
                                        mResultsModelListener.onEmpty();
                                    }
                                } else {
                                    addMoreInAdapter(getCategorizedList(myResultList));
                                }

                                if(myResultList.size() != LIMIT) {
                                    hasMoreItems = false;
                                }
                            }
                        } else {
                            if (offset == 0) {
                                mResultsModelListener.onFailedMyResults(response.message());
                            }
                            if(response.code() == 404) {
                                hasMoreItems = false;
                            }
                        }
                        isLoading = false;
                    }
                }
        );
    }

    private MyResultsAdapter createAdapter(Context context) {
        return mResultAdapter = new MyResultsAdapter(context);
    }

    private void destroyAdapter() {
        if (null != mResultAdapter) {
            mResultAdapter.destroy();
            mResultAdapter = null;
        }
    }

    private void loadAdapterData(List<Feed> feedList) {
        mResultAdapter.clear();
        addMoreInAdapter(feedList);
    }

    private void addMoreInAdapter(List<Feed> feedList) {
        int count = mResultAdapter.getItemCount();
        for (Feed feed : feedList) {
            mResultAdapter.add(feed, count++);
        }
    }

    @Override
    public void checkPagination(int firstVisibleItemPosition, int childCount, int itemCount) {
        if (itemCount > 0 && !isLoading && hasMoreItems) {
            int lastVisibleItem = firstVisibleItemPosition + childCount;
            if (lastVisibleItem >= (itemCount - PAGINATION_START_AT)) {
                Log.d("Pagination", "firstVisibleItem : " + firstVisibleItemPosition
                        + ", " + "visibleItemCount : " + childCount
                        + ", " + "totalItemCount : " + itemCount);

                loadMyResults(itemCount);
            }
        }
    }

    private List<Feed> getCategorizedList(List<Match> matchList) {
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

        return feedList;
    }

    @Override
    public void onClickLeaderBoard(int position) {

    }

    public interface OnMyResultsModelListener {

        void onSuccessMyResults(MyResultsAdapter myResultsAdapter);

        void onFailedMyResults(String message);

        void onNoInternet();

        void onEmpty();

        Context getContext();

        void gotoResultsTimeline();
    }
}