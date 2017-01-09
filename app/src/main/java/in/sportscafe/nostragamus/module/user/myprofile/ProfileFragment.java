package in.sportscafe.nostragamus.module.user.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.home.OnHomeActionListener;
import in.sportscafe.nostragamus.module.play.myresultstimeline.MyResultsTimelineActivity;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.settings.SettingsActivity;
import in.sportscafe.nostragamus.module.user.badges.BadgeActivity;
import in.sportscafe.nostragamus.module.user.badges.BadgeFragment;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsActivity;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.nostragamus.module.user.powerups.PowerUpActivity;
import in.sportscafe.nostragamus.module.user.powerups.PowerUpFragment;
import in.sportscafe.nostragamus.module.user.sportselection.SportSelectionActivity;
import in.sportscafe.nostragamus.module.user.sportselection.profilesportselection.ProfileSportSelectionFragment;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileFragment extends NostragamusFragment implements ProfileView, ProfileSportSelectionFragment.OnSportSelectionChangedListener,View.OnClickListener {

    private static final int SPORTS_SELECTION_CODE = 34;

    private static final int EDIT_PROFILE_CODE = 35;

    private static final int CODE_NEW_GROUP = 24;

    private static final int GROUPS_CODE = 20;

    private static final int GROUP_INFO_CODE = 10;

    private ProfilePresenter mProfilePresenter;

    private OnHomeActionListener mHomeActionListener;

    private ViewPager mViewPager;

    private String sportsFollowed;

    private String powerUpsCount;

    private String groupsCount;

    private String badgeCount;

    private ViewPagerAdapter mpagerAdapter;

    private Boolean groupClicked=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnHomeActionListener) {
            mHomeActionListener = (OnHomeActionListener) context;
        } else {
            throw new IllegalArgumentException("The base class should implement the OnHomeActionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setClickListeners();

        this.mProfilePresenter = ProfilePresenterImpl.newInstance(this);
        this.mProfilePresenter.onCreateProfile();

    }

    private void setClickListeners() {
        findViewById(R.id.profile_btn_edit).setOnClickListener(this);
        findViewById(R.id.profile_btn_logout).setOnClickListener(this);
        //findViewById(R.id.profile_groups_parent).setOnClickListener(this);
//        findViewById(R.id.profile_ll_points_parent).setOnClickListener(this);
        //findViewById(R.id.profile_ll_powerups_parent).setOnClickListener(this);
        //findViewById(R.id.profile_ll_sports_followed_parent).setOnClickListener(this);
        //findViewById(R.id.profile_ll_badge_parent).setOnClickListener(this);
        findViewById(R.id.profile_iv_image).setOnClickListener(this);
        findViewById(R.id.join_grp_btn).setOnClickListener(this);
    }

    @Override
    public void setName(String name) {
            Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        TextView tvName=(TextView) findViewById(R.id.profile_tv_title);
        tvName.setText(name);
        tvName.setTypeface(tftitle);
    }

    @Override
    public void setProfileImage(String imageUrl) {
        ((RoundImage) findViewById(R.id.profile_iv_image)).setImageUrl(
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
        groupsCount = String.valueOf(GroupsCount);
    }


    @Override
    public void setPowerUpsCount(int PowerUpsCount) {
//        TextView tvPowerUp=(TextView) findViewById(R.id.profile_tv_powerups);
//        tvPowerUp.setText(String.valueOf(PowerUpsCount));
        powerUpsCount = String.valueOf(PowerUpsCount);
    }


    @Override
    public void setPoints(long points) {
        TextView tvPoints=(TextView) findViewById(R.id.profile_tv_points);
        tvPoints.setText(String.valueOf(points)+" Points");
    }

    @Override
    public void setBadgesCount(int badgesCount) {

        List<String> badgeList = NostragamusDataHandler.getInstance().getBadgeList();

        LinearLayout parent = (LinearLayout)findViewById(R.id.badges_ll);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams)parent.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        parent.setLayoutParams(layoutParams);

        if(badgeList.size() <= 8) {

            LinearLayout layout2 = new LinearLayout(getContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.addView(layout2);
            layout2.setGravity(Gravity.CENTER_HORIZONTAL);


            for (int i = 0; i < badgeList.size(); i++) {
                String[] parts = badgeList.get(i).split("\\$");

                String badge_id = parts[0];

                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                imageView.getLayoutParams().height = 60;
                imageView.getLayoutParams().width = 60;

                switch (badge_id) {
                    case "accuracy_streak":
                        imageView.setBackgroundResource(R.drawable.notification_accuracy_badge);
                        layout2.addView(imageView);
                        break;
                    case "table_topper":
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                    default:
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                }

            }

        }else if(badgeList.size()>8) {

            LinearLayout layout2 = new LinearLayout(getContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.addView(layout2);
            layout2.setGravity(Gravity.CENTER_HORIZONTAL);

            for (int i = 0; i < 8; i++) {
                String[] parts = badgeList.get(i).split("\\$");

                String badge_id = parts[0];

                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                imageView.getLayoutParams().height = 60;
                imageView.getLayoutParams().width = 60;

                switch (badge_id) {
                    case "accuracy_streak":
                        imageView.setBackgroundResource(R.drawable.notification_accuracy_badge);
                        layout2.addView(imageView);
                        break;
                    case "table_topper":
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                    default:
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                }
            }


                LinearLayout layout3 = new LinearLayout(getContext());
                layout3.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                parent.addView(layout3);
                layout3.setGravity(Gravity.CENTER_HORIZONTAL);

            if(badgeList.size()<=16){

                for (int j = 8; j < badgeList.size(); j++) {

                    ImageView imageView2 = new ImageView(getContext());
                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView2.getLayoutParams().height = 60;
                    imageView2.getLayoutParams().width = 60;

                    String[] part = badgeList.get(j).split("\\$");
                    String badgeId = part[0];

                    switch (badgeId) {
                        case "accuracy_streak":
                            imageView2.setBackgroundResource(R.drawable.notification_accuracy_badge);
                            layout3.addView(imageView2);
                            break;
                        case "table_topper":
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                        default:
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                    }
                }

            }else if (badgeList.size()>16){

                for (int j = 8; j < 16; j++) {

                    ImageView imageView2 = new ImageView(getContext());
                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView2.getLayoutParams().height = 60;
                    imageView2.getLayoutParams().width = 60;

                    String[] part = badgeList.get(j).split("\\$");
                    String badgeId = part[0];

                    switch (badgeId) {
                        case "accuracy_streak":
                            imageView2.setBackgroundResource(R.drawable.notification_accuracy_badge);
                            layout3.addView(imageView2);
                            break;
                        case "table_topper":
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                        default:
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                    }
                }

                TextView textview = new TextView(getContext());
                RelativeLayout.LayoutParams lpTextView = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                textview.setLayoutParams(lpTextView);
                textview.setPadding(5,10,5,5);
                textview.setText("+ " + (badgeList.size()-16) + " More");
                textview.setTextColor(Color.WHITE);
                layout3.addView(textview);

            }

        }

        if (badgesCount==0){
            badgeCount = "0";
        }
        badgeCount = String.valueOf(badgesCount);

    }


    @Override
    public void initMyPosition(UserInfo userInfo, LbSummary lbSummary) {
        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setOffscreenPageLimit(5);
        mpagerAdapter = getAdapter(userInfo, lbSummary);
        mViewPager.setAdapter(mpagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                NostragamusAnalytics.getInstance().trackUserProfile(AnalyticsActions.TABS,
                        mViewPager.getAdapter().getPageTitle(position).toString());

                Button createGroupbtn = (Button)findViewById(R.id.join_grp_btn);
                if(position==1){
                    createGroupbtn.setVisibility(View.VISIBLE);
                }else {
                    createGroupbtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(mViewPager);

        LinearLayout linearLayout = (LinearLayout)tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getContext().getResources().getColor(R.color.profile_tab_line_color));
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            RelativeLayout relativeLayout = (RelativeLayout)
//                    LayoutInflater.from(isThreadAlive()).inflate(R.layout.inflater_profile_tab_layout, tabLayout, false);
//
//            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
//            tabTextView.setText(tab.getText());
//            tab.setCustomView(relativeLayout);
//            tab.select();
//        }

    }

    private ViewPagerAdapter getAdapter(UserInfo userInfo, LbSummary lbSummary) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(TimelineFragment.newInstance(userInfo.getId() + ""),
                userInfo.getTotalMatchesPlayed() + "\n Matches");
        pagerAdapter.addFragment(PowerUpFragment.newInstance(), powerUpsCount+"\n Powerups");
        pagerAdapter.addFragment(AllGroupsFragment.newInstance(), groupsCount+ "\n Groups");
        pagerAdapter.addFragment(ProfileSportSelectionFragment.newInstance(this), sportsFollowed + " \n Sports");
//        pagerAdapter.addFragment(ChallengesFragment.newInstance(lbSummary.getChallenges()), "0  \n Challenges");
        pagerAdapter.addFragment(BadgeFragment.newInstance(), badgeCount+"\n Badges");
        return pagerAdapter;
    }

    @Override
    public void navigateToLogIn() {
        Intent intent = new Intent(getContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToPowerUpScreen() {
        Intent intent = new Intent(getContext(), PowerUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToBadgeScreen() {
        Intent intent = new Intent(getContext(), BadgeActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_ll_points_parent:
                navigateToMyResults();
                break;
//            case R.id.profile_ll_sports_followed_parent:
//                navigateToSportSelection();
//                break;
            case R.id.profile_btn_edit:
                navigateToEditProfile();
                break;
            case R.id.profile_iv_image:
                navigateToEditProfile();
                break;
//            case R.id.profile_groups_parent:
//                navigateToNewGroup();
//                break;
//            case R.id.profile_ll_powerups_parent:
//                navigateToPowerUpScreen();
//                break;
//            case R.id.profile_ll_badge_parent:
//                navigateToBadgeScreen();
//                break;
            case R.id.profile_btn_logout:
                navigateToSettings();
                break;
            case R.id.join_grp_btn:
                navigateToJoinGroup();
                break;
        }
    }

    private void navigateToMyResults() {
        startActivity(new Intent(getContext(), MyResultsTimelineActivity.class));
        getActivity().finish();
    }

    private void navigateToSettings() {
        startActivity(new Intent(getContext(), SettingsActivity.class));
    }

    @Override
    public void navigateToJoinGroup() {
        startActivity(new Intent(getContext(), JoinGroupActivity.class));
    }

    private void navigateToSportSelection() {
        Intent intent = new Intent(getContext(), SportSelectionActivity.class);
        intent.putExtra(Constants.BundleKeys.FROM_PROFILE, true);
        startActivityForResult(intent, SPORTS_SELECTION_CODE);
    }

    private void navigateToEditProfile() {
        Intent intent=new Intent(getContext(),EditProfileActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("screen",Constants.BundleKeys.HOME_SCREEN);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_PROFILE_CODE);
    }

    private void navigateToNewGroup() {
        Intent intent = new Intent(getContext(), AllGroupsActivity.class);
        startActivityForResult(intent, GROUPS_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK == resultCode) {
            if(SPORTS_SELECTION_CODE == requestCode) {
                mProfilePresenter.onGetSportsSelectionResult();
            }
            if(GROUPS_CODE == requestCode) {
                mProfilePresenter.onGetUpdatedNumberofGroups();
                mProfilePresenter.onCreateProfile();
            }
            else if(EDIT_PROFILE_CODE == requestCode) {
                mProfilePresenter.onEditProfileDone();
            }
            else {
                mProfilePresenter.onGroupDetailsUpdated();
            }
        }
    }

    @Override
    public void setSportsCount(int sportsCount) {

        if(sportsCount==1){
            mpagerAdapter.updateTitle(2,sportsCount+ " \n Sport");
        }
        else {
            mpagerAdapter.updateTitle(2,sportsCount+ " \n Sports");
        }
    }
}