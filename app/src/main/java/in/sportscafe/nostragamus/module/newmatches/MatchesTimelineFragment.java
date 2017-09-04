package in.sportscafe.nostragamus.module.newmatches;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;


import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendFragment;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;

/**
 * Created by deepanshi on 9/1/17.
 */

public class MatchesTimelineFragment extends BaseFragment implements MatchesTimelineAdapterListener {

    private static final String TAG = ReferFriendFragment.class.getSimpleName();

    private MatchesTimelineFragmentListener matchesTimelineFragmentListener;

    private RecyclerView mRcvMatches;

    private MatchesTimelineAdapter mTimelineAdapter;

    public MatchesTimelineFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MatchesTimelineActivity) {
            matchesTimelineFragmentListener = (MatchesTimelineFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_matches_timeline, container, false);

        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {

        mRcvMatches = (RecyclerView)rootView.findViewById(R.id.match_timeline_rv);
        mRcvMatches.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvMatches.setHasFixedSize(true);

        mTimelineAdapter = new MatchesTimelineAdapter(getContext(), this);
        mRcvMatches.setAdapter(mTimelineAdapter);

        addInitialMatches();
    }

    private void addInitialMatches() {
        if (null == mTimelineAdapter) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addInitialMatches();
                }
            }, 250);
        } else {
            refreshMatches(getNewChallengeResponse());
        }
    }

    private NewChallengesResponse getNewChallengeResponse() {
        NewChallengesResponse newChallengesResponse = null;
        Bundle args = getArguments();
        if (args != null) {
            newChallengesResponse = Parcels.unwrap(getArguments().getParcelable(Constants.BundleKeys.NEW_CHALLENGES_RESPONSE));
        }
        return newChallengesResponse;
    }


    public void refreshMatches(NewChallengesResponse newChallengesResponse) {

        if (mTimelineAdapter != null) {
            mTimelineAdapter.clear();
            mTimelineAdapter.addAll(newChallengesResponse.getMatches());
            mRcvMatches.scrollToPosition(0);
        }

         /* Empty list view */
        if (getActivity() != null && getView() != null && mTimelineAdapter != null) {
            if (newChallengesResponse.getMatches() == null || newChallengesResponse.getMatches().isEmpty()) {
                mRcvMatches.setVisibility(View.GONE);
//                LinearLayout noHistoryLayout = (LinearLayout) getView().findViewById(R.id.referral_credits_no_history_layout);
//                noHistoryLayout.setVisibility(View.VISIBLE);
            } else {
                mRcvMatches.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showJoinContest(Challenge challenge) {

    }
}
