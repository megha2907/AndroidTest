package in.sportscafe.nostragamus.module.user.playerprofile;


import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.comparisons.PlayerComparisonActivity;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.playerbadges.PlayerBadgeFragment;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;


/**
 * Created by deepanshi on 12/22/16.
 */

public class PlayerProfileActivity extends NostragamusActivity implements PlayerProfileView, View.OnClickListener {

    private String sportsFollowed;

    private String groupsCount;

    private String badgeCount;

    private PlayerProfilePresenter mProfilePresenter;

    private ViewPagerAdapter mpagerAdapter;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        this.mProfilePresenter = PlayerProfilePresenterImpl.newInstance(this);
        this.mProfilePresenter.onCreateProfile(getIntent().getExtras());

        Button compareProfile = (Button) findViewById(R.id.player_profile_btn_compare_profile);
        compareProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.player_profile_btn_back:
                finish();
                break;

            case R.id.player_profile_btn_compare_profile:
                mProfilePresenter.onClickCompareProfile();
                break;
        }
    }

    @Override
    public void setName(String name) {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        TextView tvName = (TextView) findViewById(R.id.player_profile_tv_title);
        tvName.setText(name);
        tvName.setTypeface(tftitle);
    }

    @Override
    public void setProfileImage(String imageUrl) {
        ((HmImageView) findViewById(R.id.player_profile_iv_image)).setImageUrl(
                imageUrl
        );
    }

    @Override
    public void setSportsFollowedCount(int sportsFollowedCount) {
//        TextView tvsports=(TextView) findViewById(R.id.profile_tv_sports_folld);
//        tvsports.setText(String.valueOf(sportsFollowedCount));
        sportsFollowed = String.valueOf(sportsFollowedCount);

    }

    @Override
    public void setGroupsCount(int GroupsCount) {
//        TextView tvGroup=(TextView) findViewById(R.id.profile_number_of_groups);
//        tvGroup.setText(String.valueOf(GroupsCount));
        if (GroupsCount == 0) {
            groupsCount = "0";
        } else {
            groupsCount = String.valueOf(GroupsCount);
        }
    }


    @Override
    public void setLevel(String level) {
        TextView tvLevel = (TextView) findViewById(R.id.player_profile_tv_level);
        tvLevel.setText("Level " + level);
    }

    @Override
    public void setPoints(long points) {
        TextView tvPoints = (TextView) findViewById(R.id.player_profile_tv_points);
        tvPoints.setText(String.valueOf(points));
    }

    @Override
    public void setAccuracy(int accuracy) {
        Button tvPoints = (Button) findViewById(R.id.player_profile_btn_accuracy);
        tvPoints.setText(String.valueOf(accuracy + "%"));
    }

    @Override
    public void setPredictionCount(Integer predictionCount) {
        Button tvPredictionCount = (Button) findViewById(R.id.player_profile_btn_predictions);
        tvPredictionCount.setText(String.valueOf(predictionCount));
    }

    @Override
    public void navigateToPlayerComparison(Bundle playerInfoBundle) {
        Intent intent = new Intent(getContext(), PlayerComparisonActivity.class);
        intent.putExtras(playerInfoBundle);
        startActivity(intent);
    }

    @Override
    public void setBadgesCount(int badgesCount, List<Badge> badgeList) {
        if (badgesCount == 0) {
            badgeCount = "0";
        }
        badgeCount = String.valueOf(badgesCount);

    }


    @Override
    public void initMyPosition(PlayerInfo playerInfo, int roomId) {
        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setOffscreenPageLimit(5);
        mpagerAdapter = getAdapter(playerInfo, roomId);
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

    private ViewPagerAdapter getAdapter(PlayerInfo playerInfo, int roomId) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(TimelineFragment.newInstance(playerInfo, roomId), "Matches");

        if (badgeCount == "1") {
            pagerAdapter.addFragment(PlayerBadgeFragment.newInstance(playerInfo), "Achievement");
        } else {
            pagerAdapter.addFragment(PlayerBadgeFragment.newInstance(playerInfo), "Achievements");
        }
        return pagerAdapter;
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.PLAYER_PROFILE;
    }

}
