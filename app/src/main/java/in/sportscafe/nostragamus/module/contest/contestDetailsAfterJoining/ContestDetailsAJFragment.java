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
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.ui.ContestEntriesViewPagerFragment;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayMatchTimelineViewPagerFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.PointsFragment;

/**
 * Created by deepanshi on 9/13/17.
 */

public class ContestDetailsAJFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = ContestDetailsAJFragment.class.getSimpleName();

    private ContestDetailsAJFragmentListener mContestDetailsAJFragmentListener;

    public ContestDetailsAJFragment() {
    }

    TextView mTvTBarHeading;
    TextView mTvTBarSubHeading;
    TextView mTvTBarWalletMoney;
    String challengeName;

    private ContestDetailsAJViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ContestDetailsAJActivity) {
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

    private void setInfo(Contest contest) {

        if (contest != null) {
            mTvTBarHeading.setText(contest.getConfigName());
            mTvTBarSubHeading.setText("India vs Aus T20");
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
            Contest contest = Parcels.unwrap(arguments.getParcelable(Constants.BundleKeys.CONTEST));
            createAdapter(contest);
            setInfo(contest);
        }
    }

    private void createAdapter(Contest contest) {

        ViewPager mViewPager = (ViewPager) getView().findViewById(R.id.contest_details_viewPager);
        mViewPagerAdapter = new ContestDetailsAJViewPagerAdapter(getChildFragmentManager(), getContext());

        if (contest != null) {

            InPlayMatchTimelineViewPagerFragment matchTimelineViewPagerFragment = new InPlayMatchTimelineViewPagerFragment();
            mViewPagerAdapter.addFragment(matchTimelineViewPagerFragment, Constants.ContestDetailsTabs.MATCHES);

            PointsFragment pointsFragment = PointsFragment.newInstance(contest);
            mViewPagerAdapter.addFragment(pointsFragment, Constants.ContestDetailsTabs.LEADERBOARDS);

            RewardsFragment rewardsFragment = RewardsFragment.newInstance(contest.getContestId());
            mViewPagerAdapter.addFragment(rewardsFragment, Constants.ContestDetailsTabs.PRIZES);

            RulesFragment rulesFragment = RulesFragment.newInstance(contest);
            mViewPagerAdapter.addFragment(rulesFragment, Constants.ContestDetailsTabs.RULES);

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOffscreenPageLimit(4);

            setTabLayout(mViewPager);

        }

    }

    private void setTabLayout(ViewPager mViewPager) {

        try {

            TabLayout contestTabLayout = (TabLayout) getView().findViewById(R.id.contest_details_tabs);
            contestTabLayout.setupWithViewPager(mViewPager);

            if (mViewPagerAdapter != null) {
                TabLayout.Tab tab = contestTabLayout.getTabAt(0);
                if (tab != null) {
                    contestTabLayout.getTabAt(0).setCustomView(mViewPagerAdapter.getTabView(0));
                    contestTabLayout.getTabAt(1).setCustomView(mViewPagerAdapter.getTabView(1));
                    contestTabLayout.getTabAt(2).setCustomView(mViewPagerAdapter.getTabView(2));
                    contestTabLayout.getTabAt(3).setCustomView(mViewPagerAdapter.getTabView(3));
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
