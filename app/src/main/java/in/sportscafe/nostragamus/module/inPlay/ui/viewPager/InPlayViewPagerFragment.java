package in.sportscafe.nostragamus.module.inPlay.ui.viewPager;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.contestDetails.ContestDetailsActivity;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.ui.matches.MatchesTimelineActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayViewPagerFragment extends BaseFragment {

    private static final String TAG = InPlayViewPagerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SportsTab mSportsTab;
    private List<InPlayResponse> mFilteredContests;

    public InPlayViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inplay_viewpager, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.inplay_contest_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (mRecyclerView != null && mFilteredContests != null) {
            mRecyclerView.setAdapter(new InPlayRecyclerAdapter(mRecyclerView.getContext(), mFilteredContests, getAdapterListener()));
        }
    }

    @NonNull
    private InPlayAdapterListener getAdapterListener() {
        return new InPlayAdapterListener() {
            @Override
            public void onJoinedContestCardClicked(Bundle args) {

            }

            @Override
            public void onJoinedContestMoreContestButtonClicked(Bundle args) {

            }

            @Override
            public void onJoinedContestWinningsClicked(Bundle args) {

            }

            @Override
            public void onCompletedCardClicked(Bundle args) {

            }

            @Override
            public void onCompletedWinningClicked(Bundle args) {

            }

            @Override
            public void onHeadLessContestCardClicked(Bundle args) {

            }

            @Override
            public void onHeadLessJoinButtonClicked(Bundle args) {

            }
        };
    }


    private void goToNewMatchesTimeline(Bundle args) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), ContestDetailsActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }

    public void setTabDetails(SportsTab sportsTab) {
        mSportsTab = sportsTab;
    }

    public SportsTab getTabDetails() {
        return mSportsTab;
    }

    public void onChallengeData(List<InPlayResponse> inPlayFilterred) {
        mFilteredContests = inPlayFilterred;

        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
