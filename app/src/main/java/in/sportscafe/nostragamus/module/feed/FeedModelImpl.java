package in.sportscafe.nostragamus.module.feed;

import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.feed.dto.FeedResponse;
import in.sportscafe.nostragamus.module.feed.dto.FeedTimeline;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.TournamentPowerupInfo;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedModelImpl implements FeedModel {

    private boolean mFeedRefreshed = false;

    private int mClosestDatePosition = 0;

    private TimelineAdapter mFeedAdapter;

    private OnFeedModelListener mFeedModelListener;

    private String mTournamentName;

    private Integer mTourId;

    private TournamentPowerupInfo mPowerUpInfo;

    private FeedModelImpl(OnFeedModelListener listener) {
        this.mFeedModelListener = listener;
    }

    public static FeedModel newInstance(OnFeedModelListener listener) {
        return new FeedModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mTournamentName = bundle.getString(BundleKeys.TOURNAMENT_NAME);
        mTourId = bundle.getInt(BundleKeys.TOURNAMENT_ID);
    }

    @Override
    public TimelineAdapter getAdapter(Context context, List<Match> matchList) {
        mFeedAdapter = new TimelineAdapter(context, mPowerUpInfo);
        for (Match match : matchList) {
            mFeedAdapter.add(match);
        }
        return mFeedAdapter;
    }

    @Override
    public void getFeeds() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callFeedListApi(mTourId);
        } else {
            mFeedModelListener.onNoInternet();
        }
    }

    @Override
    public void destroyAll() {
        if (null != mFeedAdapter) {
            mFeedAdapter.destroy();
        }
    }

    @Override
    public String getTournamentName() {
        return mTournamentName;
    }

    @Override
    public void refreshFeed() {
        mFeedRefreshed = true;

        mFeedAdapter.clear();
        mFeedAdapter.notifyDataSetChanged();

        getFeeds();
    }

    @Override
    public boolean isFeedRefreshed() {
        return mFeedRefreshed;
    }

    private void callFeedListApi(Integer tourId) {
        MyWebService.getInstance().getMatches(tourId).enqueue(new NostragamusCallBack<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    FeedTimeline feedTimeline = response.body().getFeedTimeline();
                    List<Match> matchList = feedTimeline.getMatches();
                    if (null == matchList || matchList.isEmpty()) {
                        mFeedModelListener.onEmpty();
                        return;
                    }

                    mPowerUpInfo = feedTimeline.getTournamentPowerupInfo();

                    HashMap<String, Integer> powerUpMap = mPowerUpInfo.getPowerUps();
                    mFeedModelListener.onSuccessFeeds(
                            matchList,
                            powerUpMap.get(Powerups.XX),
                            powerUpMap.get(Powerups.NO_NEGATIVE),
                            powerUpMap.get(Powerups.AUDIENCE_POLL),
                            feedTimeline.getPowerupText()
                    );
                } else {
                    mFeedModelListener.onFailedFeeds(response.message());
                }
            }
        });
    }

    public interface OnFeedModelListener {

        void onSuccessFeeds(List<Match> matchList, Integer powerUp2x, Integer powerUpNonEgs, Integer powerUpAudiencePoll,
                            String powerUpText);

        void onFailedFeeds(String message);

        void onNoInternet();

        void onEmpty();
    }
}