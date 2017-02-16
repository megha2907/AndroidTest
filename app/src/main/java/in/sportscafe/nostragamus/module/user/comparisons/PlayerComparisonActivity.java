package in.sportscafe.nostragamus.module.user.comparisons;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards.CompareLeaderBoardFragment;
import in.sportscafe.nostragamus.module.user.comparisons.compareprofiles.CompareProfileFragment;

/**
 * Created by deepanshi on 2/11/17.
 */

public class PlayerComparisonActivity extends NostragamusActivity implements PlayerComparisonView, View.OnClickListener {

    private PlayerComparisonPresenter mPlayerComparisonPresenter;

    private ViewPagerAdapter mpagerAdapter;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_comparison);

        this.mPlayerComparisonPresenter = PlayerComparisonPresenterImpl.newInstance(this);
        this.mPlayerComparisonPresenter.onCreatePlayerComparison(getIntent().getExtras());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.player_profile_btn_back:
                finish();
                break;
        }
    }

    @Override
    public void setName(String userName, String playerName) {
        TextView tvUserName = (TextView) findViewById(R.id.player_comparison_user_tv_title);
        TextView tvPlayerName = (TextView) findViewById(R.id.player_comparison_other_player_name);
        tvUserName.setText(userName);
        tvPlayerName.setText(playerName);
    }

    @Override
    public void setProfileImage(String userImageUrl, String playerImageUrl) {
        ((RoundImage) findViewById(R.id.player_comparison_user_iv_image)).setImageUrl(
                userImageUrl
        );
        ((RoundImage) findViewById(R.id.player_comparison_other_player_image)).setImageUrl(
                playerImageUrl
        );
    }

    @Override
    public void initMyPosition(Bundle bundle) {

        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setOffscreenPageLimit(5);
        mpagerAdapter = getAdapter(bundle);
        mViewPager.setAdapter(mpagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                NostragamusAnalytics.getInstance().trackOtherProfile(
                        mViewPager.getAdapter().getPageTitle(position).toString()
                );
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(mViewPager);

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getContext().getResources().getColor(R.color.profile_tab_line_color));
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

    }

    private ViewPagerAdapter getAdapter(Bundle bundle) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(CompareProfileFragment.newInstance(bundle), "PROFILE");
        pagerAdapter.addFragment(CompareLeaderBoardFragment.newInstance(bundle), "LEADERBOARDS");

        return pagerAdapter;
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.PLAYER_PROFILE;
    }
}
