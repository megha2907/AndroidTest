package in.sportscafe.nostragamus.module.user.playerprofile;


import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.user.badges.Badge;
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
    public void setName(String name) {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        TextView tvName = (TextView) findViewById(R.id.player_profile_tv_title);
        tvName.setText(name);
        tvName.setTypeface(tftitle);
    }

    @Override
    public void setProfileImage(String imageUrl) {
        ((RoundImage) findViewById(R.id.player_profile_iv_image)).setImageUrl(
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
        tvLevel.setText("Level "+level);
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
    public void setBadgesCount(int badgesCount, List<Badge> badgeList) {

//        LinearLayout parent = (LinearLayout)findViewById(R.id.badges_ll);
//        RelativeLayout.LayoutParams layoutParams =
//                (RelativeLayout.LayoutParams)parent.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        parent.setLayoutParams(layoutParams);
//
//        if(badgeList.size() <= 8) {
//
//            LinearLayout layout2 = new LinearLayout(isThreadAlive());
//            layout2.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//            parent.setOrientation(LinearLayout.VERTICAL);
//            parent.addView(layout2);
//
//
//            for (int i = 0; i < badgeList.size(); i++) {
//                String[] parts = badgeList.get(i).split("\\$");
//
//                String badge_id = parts[0];
//
//                ImageView imageView = new ImageView(isThreadAlive());
//                imageView.setLayoutParams(new RelativeLayout.LayoutParams
//                        (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                imageView.getLayoutParams().height = 60;
//                imageView.getLayoutParams().width = 60;
//
//                switch (badge_id) {
//                    case "accuracy_streak":
//                        imageView.setBackgroundResource(R.drawable.notification_accuracy_badge);
//                        layout2.addView(imageView);
//                        break;
//                    case "table_topper":
//                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
//                        layout2.addView(imageView);
//                        break;
//                    default:
//                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
//                        layout2.addView(imageView);
//                        break;
//                }
//
//            }
//
//        }else if(badgeList.size()>8) {
//
//            LinearLayout layout2 = new LinearLayout(isThreadAlive());
//            layout2.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//            parent.setOrientation(LinearLayout.VERTICAL);
//            parent.addView(layout2);
//
//            for (int i = 0; i < 8; i++) {
//                String[] parts = badgeList.get(i).split("\\$");
//
//                String badge_id = parts[0];
//
//                ImageView imageView = new ImageView(isThreadAlive());
//                imageView.setLayoutParams(new RelativeLayout.LayoutParams
//                        (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                imageView.getLayoutParams().height = 60;
//                imageView.getLayoutParams().width = 60;
//
//                switch (badge_id) {
//                    case "accuracy_streak":
//                        imageView.setBackgroundResource(R.drawable.notification_accuracy_badge);
//                        layout2.addView(imageView);
//                        break;
//                    case "table_topper":
//                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
//                        layout2.addView(imageView);
//                        break;
//                    default:
//                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
//                        layout2.addView(imageView);
//                        break;
//                }
//            }
//
//
//            LinearLayout layout3 = new LinearLayout(isThreadAlive());
//            layout3.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//            parent.addView(layout3);
//
//            if(badgeList.size()<=16){
//
//                for (int j = 8; j < badgeList.size(); j++) {
//
//                    ImageView imageView2 = new ImageView(isThreadAlive());
//                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams
//                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                    imageView2.getLayoutParams().height = 60;
//                    imageView2.getLayoutParams().width = 60;
//
//                    String[] part = badgeList.get(j).split("\\$");
//                    String badgeId = part[0];
//
//                    switch (badgeId) {
//                        case "accuracy_streak":
//                            imageView2.setBackgroundResource(R.drawable.notification_accuracy_badge);
//                            layout3.addView(imageView2);
//                            break;
//                        case "table_topper":
//                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
//                            layout3.addView(imageView2);
//                            break;
//                        default:
//                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
//                            layout3.addView(imageView2);
//                            break;
//                    }
//                }
//
//            }else if (badgeList.size()>16){
//
//                for (int j = 8; j < 16; j++) {
//
//                    ImageView imageView2 = new ImageView(isThreadAlive());
//                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams
//                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                    imageView2.getLayoutParams().height = 60;
//                    imageView2.getLayoutParams().width = 60;
//
//                    String[] part = badgeList.get(j).split("\\$");
//                    String badgeId = part[0];
//
//                    switch (badgeId) {
//                        case "accuracy_streak":
//                            imageView2.setBackgroundResource(R.drawable.notification_accuracy_badge);
//                            layout3.addView(imageView2);
//                            break;
//                        case "table_topper":
//                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
//                            layout3.addView(imageView2);
//                            break;
//                        default:
//                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
//                            layout3.addView(imageView2);
//                            break;
//                    }
//                }
//
//                TextView textview = new TextView(isThreadAlive());
//                RelativeLayout.LayoutParams lpTextView = new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.WRAP_CONTENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT);
//                textview.setLayoutParams(lpTextView);
//                textview.setPadding(5,10,5,5);
//                textview.setText("+ " + (badgeList.size()-16) + " More");
//                textview.setTextColor(Color.WHITE);
//                layout3.addView(textview);
//
//            }
//
//        }
//
//        if (badgesCount==0){
//            badgeCount = "0";
//        }
//        badgeCount = String.valueOf(badgesCount);

    }


    @Override
    public void initMyPosition(PlayerInfo playerInfo) {

        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setOffscreenPageLimit(5);
        mpagerAdapter = getAdapter(playerInfo);
        mViewPager.setAdapter(mpagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                NostragamusAnalytics.getInstance().trackUserProfile(Constants.AnalyticsActions.TABS,
                        mViewPager.getAdapter().getPageTitle(position).toString());

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

    private ViewPagerAdapter getAdapter(PlayerInfo playerInfo) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(TimelineFragment.newInstance(playerInfo.getId()),
                playerInfo.getTotalMatchesPlayed() + "\n Matches");

        if (groupsCount == "1") {
            pagerAdapter.addFragment(AllGroupsFragment.newMutualGroupInstance(playerInfo), groupsCount + "\n Mutual Group");
        } else {
            pagerAdapter.addFragment(AllGroupsFragment.newMutualGroupInstance(playerInfo), groupsCount + "\n Mutual Groups");
        }
        if (badgeCount == "1") {
            pagerAdapter.addFragment(PlayerBadgeFragment.newInstance(playerInfo), badgeCount + "\n Badge");
        } else {
            pagerAdapter.addFragment(PlayerBadgeFragment.newInstance(playerInfo), badgeCount + "\n Badges");
        }
        return pagerAdapter;
    }

}
