package in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsFragment;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsLaunchedFrom;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardScreenData;
import in.sportscafe.nostragamus.module.challengeRules.RulesFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.dto.PoolPrizeEstimationScreenData;
import in.sportscafe.nostragamus.module.contest.dto.bumper.BumperPrizesEstimationScreenData;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.privateContest.ui.PrivateContestInPlayInviteFragment;
import in.sportscafe.nostragamus.module.contest.ui.bumperContest.BumperPrizesEstimationFragment;
import in.sportscafe.nostragamus.module.contest.ui.poolContest.PoolPrizesEstimationFragment;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayMatchesPagerFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;

/**
 * Created by deepanshi on 9/13/17.
 */

public class InPlayContestDetailsFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = InPlayContestDetailsFragment.class.getSimpleName();

    public InPlayContestDetailsFragment() {
    }

    private ContestDetailsAJFragmentListener mContestDetailsAJFragmentListener;
    private TextView mTvTBarHeading;
    private TextView mTvTBarSubHeading;
    private ViewPager mViewPager;
    private ContestDetailsAfterJoinViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof InplayContestDetailsActivity) {
            mContestDetailsAJFragmentListener = (ContestDetailsAJFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


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

    private void setInfo(InPlayContestDto inPlayContestDto) {

        if (inPlayContestDto != null) {
            mTvTBarHeading.setText(inPlayContestDto.getContestName());
            mTvTBarSubHeading.setText(inPlayContestDto.getChallengeName());
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
            InPlayContestDto inplayContest = Parcels.unwrap(arguments.getParcelable(Constants.BundleKeys.INPLAY_CONTEST));
            createAdapter(inplayContest);
            setInfo(inplayContest);

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

                    case DetailScreensLaunchRequest.MATCHES_RULES_SCREEN:
                        mViewPager.setCurrentItem(3);   // Fourth TAB is rules
                        break;

                    case DetailScreensLaunchRequest.MATCHES_INVITE_PRIVATE_CONTEST_SCREEN:
                        mViewPager.setCurrentItem(4);   // Fifth TAB is Invite tab for Private contests
                        break;
                }
            }
        }
    }

    private void createAdapter(InPlayContestDto contestDto) {
        if (getView() != null && contestDto != null && mViewPager != null) {
            mViewPagerAdapter = new ContestDetailsAfterJoinViewPagerAdapter(getChildFragmentManager(), getContext());

            InPlayMatchesPagerFragment matchTimelineViewPagerFragment = new InPlayMatchesPagerFragment();
            if (getArguments() != null) {
                matchTimelineViewPagerFragment.setArguments(getArguments());
            }
            mViewPagerAdapter.addFragment(matchTimelineViewPagerFragment, Constants.ContestDetailsTabs.MATCHES);

            LeaderBoardFragment leaderBoardFragment = LeaderBoardFragment.newInstance(contestDto.getRoomId());
            mViewPagerAdapter.addFragment(leaderBoardFragment, Constants.ContestDetailsTabs.LEADERBOARDS);

            mViewPagerAdapter.addFragment(getRewardsFragment(contestDto), Constants.ContestDetailsTabs.PRIZES);

            RulesFragment rulesFragment = RulesFragment.newInstance(contestDto.getContestId());
            mViewPagerAdapter.addFragment(rulesFragment, Constants.ContestDetailsTabs.RULES);

            /* If contest_type is private AND challenge not started AND share_tab is true then show INVITE tab */
            if (!TextUtils.isEmpty(contestDto.getContestType()) &&
                    contestDto.getContestType().equalsIgnoreCase(Constants.ContestType.PRIVATE) &&
                    !TextUtils.isEmpty(contestDto.getChallengeStartTime()) &&
                    !DateTimeHelper.isMatchStarted(contestDto.getChallengeStartTime()) &&
                    contestDto.isShouldShowShareTab()) {

                PrivateContestInPlayInviteFragment fragment = new PrivateContestInPlayInviteFragment();
                fragment.setInplayContestDto(contestDto);
                mViewPagerAdapter.addFragment(fragment, Constants.ContestDetailsTabs.INVITE);
            }

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOffscreenPageLimit(4);

            setTabLayout(mViewPager);

            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int state) {
                }

                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            // First TAB is Games
                            NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.IN_PLAY_CONTEST_DETAILS,
                                    Constants.AnalyticsClickLabels.GAMES);
                            break;
                        case 1:
                            // Second TAB is Leaderboard
                            NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.IN_PLAY_CONTEST_DETAILS,
                                    Constants.AnalyticsClickLabels.LEADERBOARD);
                            break;
                        case 2:
                            // Third TAB is Prizes
                            NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.IN_PLAY_CONTEST_DETAILS,
                                    Constants.AnalyticsClickLabels.PRIZES);
                            break;
                        case 3:
                            // Fourth TAB is Rules
                            NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.IN_PLAY_CONTEST_DETAILS,
                                    Constants.AnalyticsClickLabels.RULES);
                            break;

                        case 4:
                            // Fifth TAB is Invite
                            NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.IN_PLAY_CONTEST_DETAILS,
                                    Constants.AnalyticsClickLabels.INVITE_PRIVATE_CONTEST);
                            break;
                    }
                }
            });
        }
    }

    @NonNull
    private Fragment getRewardsFragment(InPlayContestDto contestDto) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, RewardsLaunchedFrom.IN_PLAY_CONTEST_DETAILS);

        /* PoolEstimation fragment for PoolContest */
        if (contestDto != null && !TextUtils.isEmpty(contestDto.getContestMode())
                && contestDto.getContestMode().equalsIgnoreCase(Constants.ContestType.POOL)) {

            PoolPrizeEstimationScreenData screenData = new PoolPrizeEstimationScreenData();
            screenData.setRewardScreenLauncherParent(RewardsLaunchedFrom.IN_PLAY_CONTEST_DETAILS);
            screenData.setRoomId(contestDto.getRoomId());
            screenData.setConfigId(-1);
            screenData.setContestName(contestDto.getContestName());
            args.putParcelable(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA, Parcels.wrap(screenData));

            fragment = new PoolPrizesEstimationFragment();
            fragment.setArguments(args);

        }   /* BumperEstimation fragment for Bumper Contest AND if challenge-not started */
        else if (contestDto!=null &&
                !TextUtils.isEmpty(contestDto.getContestMode()) &&
                contestDto.getContestMode().equalsIgnoreCase(Constants.ContestType.BUMPER)) {

            BumperPrizesEstimationScreenData screenData = new BumperPrizesEstimationScreenData();
            screenData.setRewardScreenLauncherParent(RewardsLaunchedFrom.IN_PLAY_CONTEST_DETAILS);
            screenData.setRoomId(contestDto.getRoomId());
            screenData.setConfigId(-1);
            screenData.setContestName(contestDto.getContestName());
            args.putParcelable(Constants.BundleKeys.BUMPER_PRIZE_ESTIMATION_SCREEN_DATA, Parcels.wrap(screenData));

            fragment = new BumperPrizesEstimationFragment();
            fragment.setArguments(args);

        } else {
            RewardScreenData rewardScreenData = new RewardScreenData();
            rewardScreenData.setRoomId(contestDto.getRoomId());
            rewardScreenData.setConfigId(-1);
            rewardScreenData.setContestName(contestDto.getContestName());
            rewardScreenData.setPoolContest(false);
            args.putParcelable(Constants.BundleKeys.REWARDS_SCREEN_DATA, Parcels.wrap(rewardScreenData));

            fragment = new RewardsFragment();
            fragment.setArguments(args);
        }

        return fragment;
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
                if (mContestDetailsAJFragmentListener != null) {
                    mContestDetailsAJFragmentListener.onBackClicked();
                }
                break;

        }
    }
}
