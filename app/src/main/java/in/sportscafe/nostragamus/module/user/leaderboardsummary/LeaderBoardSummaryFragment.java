package in.sportscafe.nostragamus.module.user.leaderboardsummary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.challenges.ChallengesFragment;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.groups.GroupsFragment;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.sports.SportsFragment;

/**
 * Created by deepanshi on 12/22/16.
 */

public class LeaderBoardSummaryFragment  extends NostragamusFragment implements LeaderBoardSummaryView{

    private CustomViewPager mViewPager;

    private LeaderBoardSummaryPresenter mleaderBoardSummaryPresenter;

    public static LeaderBoardSummaryFragment newInstance() {
        LeaderBoardSummaryFragment fragment = new LeaderBoardSummaryFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard_summary, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mleaderBoardSummaryPresenter = LeaderBoardSummaryPresenterImpl.newInstance(this);
        this.mleaderBoardSummaryPresenter.onCreateLeaderBoard();
    }

    @Override
    public void initMyPosition(LbSummary lbSummary) {
        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setAdapter(getAdapter(lbSummary));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                NostragamusAnalytics.getInstance().trackUserProfile(Constants.AnalyticsActions.TABS,
                        mViewPager.getAdapter().getPageTitle(position).toString());
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private ViewPagerAdapter getAdapter(LbSummary lbSummary) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(SportsFragment.newInstance(lbSummary.getSports()), "Sports");
        pagerAdapter.addFragment(GroupsFragment.newInstance(lbSummary.getGroups()), "Groups");
        pagerAdapter.addFragment(ChallengesFragment.newInstance(lbSummary.getChallenges()), "Challenges");
        return pagerAdapter;
    }
}
