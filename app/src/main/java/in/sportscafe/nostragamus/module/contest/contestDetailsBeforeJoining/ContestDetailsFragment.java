package in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesScreenData;
import in.sportscafe.nostragamus.module.contest.dto.PoolPrizeEstimationScreenData;
import in.sportscafe.nostragamus.module.contest.dto.bumper.BumperPrizesEstimationScreenData;
import in.sportscafe.nostragamus.module.contest.ui.bumperContest.BumperPrizesEstimationFragment;
import in.sportscafe.nostragamus.module.contest.ui.poolContest.PoolPrizesEstimationFragment;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.contest.ui.ContestEntriesViewPagerFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;

/**
 * Created by deepanshi on 9/10/17.
 */

public class ContestDetailsFragment extends NostraBaseFragment implements
        View.OnClickListener {

    private static final String TAG = ContestDetailsFragment.class.getSimpleName();

    private ContestDetailsFragmentListener mContestDetailsFragmentListener;
    private TextView mTvTBarHeading;
    private TextView mTvTBarSubHeading;
    private TextView mTvTBarWalletMoney;
    private Button mJoinContestButton;
    private ContestDetailsViewPagerAdapter mContestDetailsViewPagerAdapter;
    private ViewPager mViewPager;

    public ContestDetailsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ContestDetailsActivity) {
            mContestDetailsFragmentListener = (ContestDetailsFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contest_details, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTvTBarHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_one);
        mTvTBarSubHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_two);
        mTvTBarWalletMoney = (TextView) rootView.findViewById(R.id.toolbar_wallet_money);
        mJoinContestButton = (Button) rootView.findViewById(R.id.join_contest_btn);
        mViewPager = (ViewPager) rootView.findViewById(R.id.contest_details_viewPager);

        mJoinContestButton.setOnClickListener(this);
        rootView.findViewById(R.id.contest_details_back_btn).setOnClickListener(this);
        rootView.findViewById(R.id.toolbar_wallet_rl).setOnClickListener(this);
    }

    private void setInfo(Contest contest) {
        if (contest != null) {
            mTvTBarHeading.setText(contest.getConfigName());
            mTvTBarSubHeading.setText(contest.getChallengeName());

            if (contest.isContestJoined()) {
                mJoinContestButton.setVisibility(View.GONE);
            } else {
                mJoinContestButton.setVisibility(View.VISIBLE);
                mJoinContestButton.setText("Pay " + Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()) + " and Join Contest");
            }
        }

        int amount = (int) WalletHelper.getTotalBalance();
        mTvTBarWalletMoney.setText(String.valueOf(amount));
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
            Contest contest = Parcels.unwrap(arguments.getParcelable(Constants.BundleKeys.CONTEST));
            createAdapter(contest);
            setInfo(contest);

            showRequestedTabScreen();
        }
    }

    private void showRequestedTabScreen() {
        Bundle args = getArguments();
        if (args != null && mViewPager != null) {
            if (args.containsKey(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST)) {

                int launchScreen = args.getInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST);
                switch (launchScreen) {

                    case DetailScreensLaunchRequest.CONTESTS_REWARDS_SCREEN:
                        mViewPager.setCurrentItem(1);   // Second TAB is rewards
                        break;
                    case DetailScreensLaunchRequest.CONTESTS_RULES_SCREEN:
                        mViewPager.setCurrentItem(2);   // Second TAB is rules
                        break;
                }
            }
        }
    }

    private void createAdapter(Contest contest) {
        if (contest != null && getView() != null && mViewPager != null) {
            mContestDetailsViewPagerAdapter = new ContestDetailsViewPagerAdapter(getChildFragmentManager(), getContext());

            /* Contest Entries screen */
            ContestEntriesScreenData contestScreenData = new ContestEntriesScreenData();
            contestScreenData.setChallengeId(contest.getChallengeId());
            contestScreenData.setContestId(contest.getContestId());
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_ENTRIES_SCREEN_DATA, Parcels.wrap(contestScreenData));

            ContestEntriesViewPagerFragment contestEntriesViewPagerFragment = new ContestEntriesViewPagerFragment();
            contestEntriesViewPagerFragment.setArguments(args);
            mContestDetailsViewPagerAdapter.addFragment(contestEntriesViewPagerFragment, Constants.ContestDetailsTabs.ENTRIES);

            /* Rewards */
            mContestDetailsViewPagerAdapter.addFragment(getRewardsFragment(contest), Constants.ContestDetailsTabs.PRIZES);

            /* Rules */
            RulesFragment rulesFragment = RulesFragment.newInstance(contest.getContestId());
            mContestDetailsViewPagerAdapter.addFragment(rulesFragment, Constants.ContestDetailsTabs.RULES);

            mViewPager.setAdapter(mContestDetailsViewPagerAdapter);
            mViewPager.setOffscreenPageLimit(3);

            setTabLayout(mViewPager);
        }
    }

    @NonNull
    private Fragment getRewardsFragment(Contest contest) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, RewardsLaunchedFrom.NEW_CHALLENGE_CONTEST_DETAILS);

        /* PoolEstimation fragment for PoolContest AND if challenge-not started */
        if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.POOL)) {

            PoolPrizeEstimationScreenData screenData = new PoolPrizeEstimationScreenData();
            screenData.setRewardScreenLauncherParent(RewardsLaunchedFrom.NEW_CHALLENGE_CONTEST_DETAILS);
            screenData.setRoomId(-1);
            screenData.setConfigId(contest.getContestId());
            screenData.setContestName(contest.getConfigName());
            args.putParcelable(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA, Parcels.wrap(screenData));

            fragment = new PoolPrizesEstimationFragment();
            fragment.setArguments(args);

        }
          /* BumperEstimation fragment for Bumper Contest AND if challenge-not started */
        else if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.BUMPER)) {

            BumperPrizesEstimationScreenData screenData = new BumperPrizesEstimationScreenData();
            screenData.setRewardScreenLauncherParent(RewardsLaunchedFrom.NEW_CHALLENGE_CONTEST_DETAILS);
            screenData.setRoomId(-1);
            screenData.setConfigId(contest.getContestId());
            screenData.setContestName(contest.getConfigName());
            args.putParcelable(Constants.BundleKeys.BUMPER_PRIZE_ESTIMATION_SCREEN_DATA, Parcels.wrap(screenData));

            fragment = new BumperPrizesEstimationFragment();
            fragment.setArguments(args);

        } else {
            RewardScreenData rewardScreenData = new RewardScreenData();
            rewardScreenData.setRoomId(-1);
            rewardScreenData.setConfigId(contest.getContestId());
            rewardScreenData.setContestName(contest.getConfigName());
            rewardScreenData.setPoolContest(false);
            args.putParcelable(Constants.BundleKeys.REWARDS_SCREEN_DATA, Parcels.wrap(rewardScreenData));

            fragment = new RewardsFragment();
            fragment.setArguments(args);
        }

        return fragment;
    }


    private void setTabLayout(ViewPager mViewPager) {
        if (getView() != null && mContestDetailsViewPagerAdapter != null) {
            TabLayout contestTabLayout = (TabLayout) getView().findViewById(R.id.contest_details_tabs);
            contestTabLayout.setupWithViewPager(mViewPager);

            for (int temp = 0; temp < contestTabLayout.getTabCount(); temp++) {
                TabLayout.Tab tab = contestTabLayout.getTabAt(temp);
                if (tab != null) {
                    tab.setCustomView(mContestDetailsViewPagerAdapter.getTabView(temp));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_contest_btn:
                if (mContestDetailsFragmentListener != null) {
                    mContestDetailsFragmentListener.onJoinContestClicked(getArguments());
                    NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST_DETAILS, Constants.AnalyticsClickLabels.JOIN_CONTEST);
                }
                break;
            case R.id.contest_details_back_btn:
                if (mContestDetailsFragmentListener != null) {
                    mContestDetailsFragmentListener.onBackBtnClicked();
                }
                break;
            case R.id.toolbar_wallet_rl:
                mContestDetailsFragmentListener.onWalletClicked();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST_DETAILS, Constants.AnalyticsClickLabels.WALLET);
                break;

        }
    }

}
