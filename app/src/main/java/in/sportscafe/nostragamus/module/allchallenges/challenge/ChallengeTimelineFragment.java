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

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
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

    public void addInitialMatches(final Challenge challenge) {
        if (null == mTimelineAdapter) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addInitialMatches(challenge);
                }
            }, 250);
        } else {
            refreshChallengeMatches(challenge);
        }
    }

    public void refreshChallengeMatches(Challenge challenge) {
        mTimelineAdapter.clear();
        setMatchesLeft(String.valueOf(challenge.getCountMatchesLeft()),String.valueOf(challenge.getMatches().size()));

        HashMap<String, Integer> powerupInfo = null;
        try {
            powerupInfo = challenge.getChallengeUserInfo().getPowerUps();
        } catch (Exception e) {
        }

        mTimelineAdapter.updateChallengeInfo(powerupInfo, challenge.getName(), challenge.getChallengeId());

        mTimelineAdapter.addAll(challenge.getMatches());

        mRcvFeed.scrollToPosition(0);
    }

    private void setMatchesLeft(String matchesLeft, String matches) {
        if (!TextUtils.isEmpty(matchesLeft)) {
            if (matchesLeft.equals("0")) {
                mTvMatchesLeft.setText("No Games");
            } else {
                mTvMatchesLeft.setText(matchesLeft+"/"+matches + " Games Left");
            }
        }
    }
}