package in.sportscafe.nostragamus.module.allchallenges.challenge;

/**
 * Created by deepanshi on 10/5/16.
 */

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresultstimeline.ChallengesTimelineAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelinePresenter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelinePresenterImpl;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineView;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class ChallengeTimelineFragment extends NostragamusFragment {

    private RecyclerView mRcvFeed;

    private ChallengesTimelineAdapter mTimelineAdapter;

    public static ChallengeTimelineFragment newInstance() {
        return new ChallengeTimelineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(null == savedInstanceState) {
            mRcvFeed = (RecyclerView) findViewById(R.id.feed_rv);
            mRcvFeed.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            this.mRcvFeed.setHasFixedSize(true);

            mTimelineAdapter = new ChallengesTimelineAdapter(getContext());
            mRcvFeed.setAdapter(mTimelineAdapter);
        }
    }

    public void addInitialMatches(final List<Match> matches) {
        if(null == mTimelineAdapter) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addInitialMatches(matches);
                }
            }, 250);
        } else {
            mTimelineAdapter.addAll(matches);
        }
    }

    public void refreshMatches(List<Match> matches) {
        mTimelineAdapter.clear();
        mTimelineAdapter.addAll(matches);

        mRcvFeed.scrollToPosition(0);
    }
}