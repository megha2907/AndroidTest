package in.sportscafe.scgame.module.user.leaderboardsummary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.common.CustomViewPager;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.user.myprofile.myposition.challenges.ChallengesFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.groups.GroupsFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.sports.SportsFragment;

/**
 * Created by deepanshi on 11/7/16.
 */

public class LeaderBoardSummaryActivity extends ScGameActivity implements LeaderBoardSummaryView{

    private Toolbar mtoolbar;
    private TextView mTitle;
    private CustomViewPager mViewPager;

    private LeaderBoardSummaryPresenter mleaderBoardSummaryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_summary);

        this.mleaderBoardSummaryPresenter = LeaderBoardSummaryPresenterImpl.newInstance(this);
        this.mleaderBoardSummaryPresenter.onCreateLeaderBoard();
        initToolBar();
    }

    @Override
    public void initMyPosition(LbSummary lbSummary) {
        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setAdapter(getAdapter(lbSummary));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ScGameAnalytics.getInstance().trackUserProfile(Constants.AnalyticsActions.TABS,
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
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(SportsFragment.newInstance(lbSummary.getSports()), "Sports");
        pagerAdapter.addFragment(GroupsFragment.newInstance(lbSummary.getGroups()), "Groups");
        pagerAdapter.addFragment(ChallengesFragment.newInstance(lbSummary.getChallenges()), "Challenges");
        return pagerAdapter;
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.leaderboard_summary_toolbar);
        mtoolbar.setTitle("LeaderBoard Summary");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }

        );
    }
}
