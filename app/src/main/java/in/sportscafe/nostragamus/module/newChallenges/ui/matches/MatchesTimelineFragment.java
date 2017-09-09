package in.sportscafe.nostragamus.module.newChallenges.ui.matches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;


import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.newChallenges.adapter.MatchesTimelineAdapter;
import in.sportscafe.nostragamus.module.newChallenges.adapter.MatchesTimelineAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;

/**
 * Created by deepanshi on 9/1/17.
 */

public class MatchesTimelineFragment extends BaseFragment implements MatchesTimelineAdapterListener, View.OnClickListener {

    private static final String TAG = MatchesTimelineFragment.class.getSimpleName();

    private MatchesTimelineFragmentListener matchesTimelineFragmentListener;
    private RecyclerView mRcvMatches;
    private MatchesTimelineAdapter mTimelineAdapter;
    private BottomSheetBehavior mBottomBehavior;

    public MatchesTimelineFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MatchesTimelineActivity) {
            matchesTimelineFragmentListener = (MatchesTimelineFragmentListener) context;
        } else {
            throw new RuntimeException(TAG + " Activity must implement " +
                    MatchesTimelineFragmentListener.class.getSimpleName());
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
        TextView tvContestHeading = (TextView) rootView.findViewById(R.id.matches_timeline_heading);
        TextView tvContestSubHeadingOne = (TextView) rootView.findViewById(R.id.matches_timeline_subheading_one);
        TextView tvContestSubHeadingTwo = (TextView) rootView.findViewById(R.id.matches_timeline_subheading_two);
        Button tvJoinContestBtn = (Button) rootView.findViewById(R.id.matches_timeline_btn_join);
        TextView tvMatchesLeft = (TextView) rootView.findViewById(R.id.matches_timeline_matches_left);
        TextView tvContestExpiry = (TextView) rootView.findViewById(R.id.matches_timeline_match_expires_in);

        mRcvMatches = (RecyclerView)rootView.findViewById(R.id.match_timeline_rv);
        mRcvMatches.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRcvMatches.setHasFixedSize(true);

        mTimelineAdapter = new MatchesTimelineAdapter(getContext(), this);
        mRcvMatches.setAdapter(mTimelineAdapter);

        tvJoinContestBtn.setOnClickListener(this);

        mBottomBehavior = BottomSheetBehavior.from(rootView.findViewById(R.id.bottom_sheet_frame_layout));
        mBottomBehavior.setPeekHeight((int)getResources().getDimension(R.dimen.dim_40));

        LinearLayout joinContestBottomSheetButton = (LinearLayout) rootView.findViewById(R.id.bottom_sheet_drag_me);
        joinContestBottomSheetButton.setOnClickListener(this);
    }

    private NewChallengesResponse getNewChallengeResponse() {
        NewChallengesResponse newChallengesResponse = null;
        Bundle args = getArguments();
        if (args != null) {
            newChallengesResponse = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.NEW_CHALLENGES_RESPONSE));
        }
        return newChallengesResponse;
    }

    public void refreshMatches(NewChallengesResponse newChallengesResponse) {

        if (mTimelineAdapter != null && newChallengesResponse != null) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.matches_timeline_btn_join:
                break;

            case R.id.bottom_sheet_drag_me:
                if (mBottomBehavior != null) {
                    if (mBottomBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        mBottomBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        mBottomBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
                break;

        }
    }

}
