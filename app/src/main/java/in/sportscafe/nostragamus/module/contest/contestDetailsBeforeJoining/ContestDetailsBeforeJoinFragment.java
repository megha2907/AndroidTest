package in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsFragment;
import in.sportscafe.nostragamus.module.challengeRules.RulesFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesScreenData;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.contest.ui.ContestEntriesViewPagerFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

/**
 * Created by deepanshi on 9/10/17.
 */

public class ContestDetailsBeforeJoinFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = ContestDetailsBeforeJoinFragment.class.getSimpleName();

    private ContestDetailsBJFragmentListener mContestDetailsBJFragmentListener;
    private TextView mTvTBarHeading;
    private TextView mTvTBarSubHeading;
    private TextView mTvTBarWalletMoney;
    private Button joinContest;
    private ContestDetailsBJViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;

    public ContestDetailsBeforeJoinFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ContestDetailsBeforeJoinedActivity) {
            mContestDetailsBJFragmentListener = (ContestDetailsBJFragmentListener) context;
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
        joinContest = (Button) rootView.findViewById(R.id.join_contest_btn);
        mViewPager = (ViewPager) rootView.findViewById(R.id.contest_details_viewPager);

        joinContest.setOnClickListener(this);
        rootView.findViewById(R.id.contest_details_back_btn).setOnClickListener(this);
    }

    private void setInfo(Contest contest) {
        if (contest != null) {
            mTvTBarHeading.setText(contest.getConfigName());
            mTvTBarSubHeading.setText("");
            joinContest.setText("Pay " + Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()) + " and Join Contest");
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
                }
            }
        }
    }

    private void createAdapter(Contest contest) {
        if (contest != null && getView() != null && mViewPager != null) {
            mViewPagerAdapter = new ContestDetailsBJViewPagerAdapter(getChildFragmentManager(), getContext());

            /* Contest Entries screen */
            ContestEntriesScreenData contestScreenData = new ContestEntriesScreenData();
            contestScreenData.setChallengeId(contest.getChallengeId());
            contestScreenData.setContestId(contest.getContestId());
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_ENTRIES_SCREEN_DATA, Parcels.wrap(contestScreenData));

            ContestEntriesViewPagerFragment contestEntriesViewPagerFragment = new ContestEntriesViewPagerFragment();
            contestEntriesViewPagerFragment.setArguments(args);
            mViewPagerAdapter.addFragment(contestEntriesViewPagerFragment, Constants.ContestDetailsTabs.ENTRIES);

            /* Rewards */
            RewardsFragment rewardsFragment = RewardsFragment.newInstance(contest.getContestId());
            mViewPagerAdapter.addFragment(rewardsFragment, Constants.ContestDetailsTabs.PRIZES);

            /* Rules */
            RulesFragment rulesFragment = RulesFragment.newInstance(contest.getContestId());
            mViewPagerAdapter.addFragment(rulesFragment, Constants.ContestDetailsTabs.RULES);

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOffscreenPageLimit(3);

            setTabLayout(mViewPager);
        }
    }

    private void setTabLayout(ViewPager mViewPager) {
        if (getView() != null && mViewPagerAdapter != null) {
            TabLayout contestTabLayout = (TabLayout) getView().findViewById(R.id.contest_details_tabs);
            contestTabLayout.setupWithViewPager(mViewPager);

            for (int temp = 0; temp < contestTabLayout.getTabCount(); temp++) {
                TabLayout.Tab tab = contestTabLayout.getTabAt(temp);
                if (tab != null) {
                    tab.setCustomView(mViewPagerAdapter.getTabView(temp));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_contest_btn:
                if (mContestDetailsBJFragmentListener != null) {
                    mContestDetailsBJFragmentListener.onJoinContestClicked(getArguments());
                }
                break;
            case R.id.contest_details_back_btn:
                if (mContestDetailsBJFragmentListener != null) {
                    mContestDetailsBJFragmentListener.onBackBtnClicked();
                }
                break;

        }
    }
}
