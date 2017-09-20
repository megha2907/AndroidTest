package in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining;

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
import in.sportscafe.nostragamus.module.challengeRewards.RewardsFragment;
import in.sportscafe.nostragamus.module.challengeRules.RulesFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayMatchTimelineViewPagerFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.user.leaderboard.LeaderBoardFragment;

/**
 * Created by deepanshi on 9/13/17.
 */

public class ContestDetailsAfterJoinFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = ContestDetailsAfterJoinFragment.class.getSimpleName();

    private ContestDetailsAJFragmentListener mContestDetailsAJFragmentListener;

    public ContestDetailsAfterJoinFragment() {
    }

    TextView mTvTBarHeading;
    TextView mTvTBarSubHeading;
    TextView mTvTBarWalletMoney;
    String challengeName;

    private ContestDetailsAfterJoinViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ContestDetailsAfterJoinActivity) {
            mContestDetailsAJFragmentListener = (ContestDetailsAJFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aj_contest_details, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTvTBarHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_one);
        mTvTBarSubHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_two);
        //mTvTBarWalletMoney = (TextView) rootView.findViewById(R.id.toolbar_wallet_money);
        rootView.findViewById(R.id.contest_details_back_btn).setOnClickListener(this);
    }

    private void setInfo(InPlayContestDto inPlayContestDto) {

        if (inPlayContestDto != null) {
            mTvTBarHeading.setText(inPlayContestDto.getContestName());
            mTvTBarSubHeading.setText(inPlayContestDto.getContestName());   // TODO change as per need
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
        }
    }

    private void createAdapter(InPlayContestDto contestDto) {
        if (getView() != null && contestDto != null) {
            ViewPager mViewPager = (ViewPager) getView().findViewById(R.id.contest_details_viewPager);
            mViewPagerAdapter = new ContestDetailsAfterJoinViewPagerAdapter(getChildFragmentManager(), getContext());

            InPlayMatchTimelineViewPagerFragment matchTimelineViewPagerFragment = new InPlayMatchTimelineViewPagerFragment();
            if (getArguments() != null) {
                matchTimelineViewPagerFragment.setArguments(getArguments());
            }
            mViewPagerAdapter.addFragment(matchTimelineViewPagerFragment, Constants.ContestDetailsTabs.MATCHES);

            LeaderBoardFragment leaderBoardFragment = LeaderBoardFragment.newInstance(contestDto.getRoomId());
            mViewPagerAdapter.addFragment(leaderBoardFragment, Constants.ContestDetailsTabs.LEADERBOARDS);

            RewardsFragment rewardsFragment = RewardsFragment.newInstance(contestDto.getRoomId());
            mViewPagerAdapter.addFragment(rewardsFragment, Constants.ContestDetailsTabs.PRIZES);

            RulesFragment rulesFragment = RulesFragment.newInstance(contestDto.getContestId());
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
                if (mContestDetailsAJFragmentListener != null) {
                    mContestDetailsAJFragmentListener.onBackClicked();
                }
                break;

        }
    }
}
