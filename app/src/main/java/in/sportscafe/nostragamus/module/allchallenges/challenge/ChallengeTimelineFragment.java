package in.sportscafe.nostragamus.module.allchallenges.challenge;

/**
 * Created by deepanshi on 10/5/16.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresultstimeline.ChallengesTimelineAdapter;

/**
 * Created by Jeeva on 15/6/16.
 */
public class ChallengeTimelineFragment extends NostragamusFragment {

    private TextView mTvMatchesLeft;

    private RecyclerView mRcvFeed;

    private ChallengesTimelineAdapter mTimelineAdapter;

    public static ChallengeTimelineFragment newInstance() {
        return new ChallengeTimelineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_timeline, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null == savedInstanceState) {
            mTvMatchesLeft = (TextView) findViewById(R.id.challenge_timeline_tv_match_left);

            mRcvFeed = (RecyclerView) findViewById(R.id.challenge_timeline_rv);
            mRcvFeed.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            this.mRcvFeed.setHasFixedSize(true);

            mTimelineAdapter = new ChallengesTimelineAdapter(getContext());
            mRcvFeed.setAdapter(mTimelineAdapter);
        }
    }

    public void addInitialMatches(final List<Match> matches, final String matchesLeft, final HashMap<String, Integer> powerupInfo) {
        if (null == mTimelineAdapter) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addInitialMatches(matches, matchesLeft, powerupInfo);
                }
            }, 250);
        } else {
            refreshMatches(matches, matchesLeft, powerupInfo);
        }
    }

    public void refreshMatches(List<Match> matches, String matchesLeft, HashMap<String, Integer> powerupInfo) {
        setMatchesLeft(matchesLeft);

        mTimelineAdapter.clear();
        mTimelineAdapter.setPowerupInfo(powerupInfo);
        mTimelineAdapter.addAll(matches);

        mRcvFeed.scrollToPosition(0);
    }

    private void setMatchesLeft(String matchesLeft) {
        if (!TextUtils.isEmpty(matchesLeft)) {
            if (matchesLeft.equals("0")) {
                mTvMatchesLeft.setText("No Games");
            } else {
                mTvMatchesLeft.setText(matchesLeft + " Games Left");
            }
        }
    }
}