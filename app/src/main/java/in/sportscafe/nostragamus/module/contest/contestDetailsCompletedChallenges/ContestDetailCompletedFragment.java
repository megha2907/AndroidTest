package in.sportscafe.nostragamus.module.contest.contestDetailsCompletedChallenges;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestDto;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager.CompletedMatchTimelineViewPagerFragment;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsFragment;
import in.sportscafe.nostragamus.module.challengeRules.RulesFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;

/**
 * Created by sc on 4/10/17.
 */

public class ContestDetailCompletedFragment  extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = ContestDetailCompletedFragment.class.getSimpleName();

    public ContestDetailCompletedFragment() {
    }

    private ContestDetailCompletedFragmentListener mContestDetailCompletedFragmentListener;
    private TextView mTvTBarHeading;
    private TextView mTvTBarSubHeading;
    private ViewPager mViewPager;
    private ContestDetailsCompletedViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ContestDetailCompletedActivity) {
            mContestDetailCompletedFragmentListener = (ContestDetailCompletedFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inplay_contest_details, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTvTBarHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_one);
        mTvTBarSubHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_two);
        mViewPager = (ViewPager) rootView.findViewById(R.id.contest_details_viewPager);

        rootView.findViewById(R.id.contest_details_back_btn).setOnClickListener(this);
    }

    private void setInfo(CompletedContestDto completedContestDto) {

        if (completedContestDto != null) {
            mTvTBarHeading.setText(completedContestDto.getContestName());
            mTvTBarSubHeading.setText(completedContestDto.getChallengeName());
        }

        /*int amount = (int) WalletHelper.getTotalBalance();
        mTvTBarWalletMoney.setText(String.valueOf(amount));*/
    }

    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openBundle(getArguments());
    }

    private void openBundle(Bundle arguments) {
        if (arguments != null) {
            CompletedContestDto completedContestDto = Parcels.unwrap(arguments.getParcelable(Constants.BundleKeys.COMPLETED_CONTEST));
            createAdapter(completedContestDto);
            setInfo(completedContestDto);

            showRequestedTabScreen();
        }
    }

    private void showRequestedTabScreen() {
        Bundle args = getArguments();
        if (args != null && mViewPager != null) {
            if (args.containsKey(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST)) {

                int launchScreen = args.getInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST);
                switch (launchScreen) {

                    case DetailScreensLaunchRequest.MATCHES_LEADER_BOARD_SCREEN:
                        mViewPager.setCurrentItem(1);
                        break;

                    case DetailScreensLaunchRequest.MATCHES_REWARDS_SCREEN:
                        mViewPager.setCurrentItem(2);   // Third TAB is rewards
                        break;
                }
            }
        }
    }

    private void createAdapter(CompletedContestDto completedContestDto) {
        if (getView() != null && completedContestDto != null && mViewPager != null) {
            mViewPagerAdapter = new ContestDetailsCompletedViewPagerAdapter(getChildFragmentManager(), getContext());

            CompletedMatchTimelineViewPagerFragment matchTimelineViewPagerFragment = new CompletedMatchTimelineViewPagerFragment();
            if (getArguments() != null) {
                matchTimelineViewPagerFragment.setArguments(getArguments());
            }
            mViewPagerAdapter.addFragment(matchTimelineViewPagerFragment, Constants.ContestDetailsTabs.MATCHES);

            LeaderBoardFragment leaderBoardFragment = LeaderBoardFragment.newInstance(completedContestDto.getRoomId());
            mViewPagerAdapter.addFragment(leaderBoardFragment, Constants.ContestDetailsTabs.LEADERBOARDS);

            RewardsFragment rewardsFragment = RewardsFragment.newInstance(completedContestDto.getRoomId(),-1);
            mViewPagerAdapter.addFragment(rewardsFragment, Constants.ContestDetailsTabs.WINNERS);

            RulesFragment rulesFragment = RulesFragment.newInstance(completedContestDto.getContestId());
            mViewPagerAdapter.addFragment(rulesFragment, Constants.ContestDetailsTabs.RULES);

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOffscreenPageLimit(4);


            setTabLayout(mViewPager);
        }
    }

    private void setTabLayout(ViewPager mViewPager) {
        if (getView() != null && getActivity() != null) {
            TabLayout contestTabLayout = (TabLayout) getView().findViewById(R.id.contest_details_tabs);
            contestTabLayout.setupWithViewPager(mViewPager);

            if (mViewPagerAdapter != null) {
                for (int temp = 0; temp < contestTabLayout.getTabCount(); temp++) {
                    TabLayout.Tab tab = contestTabLayout.getTabAt(temp);
                    if (tab != null) {
                        tab.setCustomView(mViewPagerAdapter.getTabView(temp));
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contest_details_back_btn:
                if (mContestDetailCompletedFragmentListener != null) {
                    mContestDetailCompletedFragmentListener.onBackClicked();
                }
                break;

        }
    }

}
