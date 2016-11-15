package in.sportscafe.scgame.module.user.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.Constants.AnalyticsActions;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.common.CustomViewPager;
import in.sportscafe.scgame.module.common.RoundImage;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.common.Settings;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.module.play.myresultstimeline.MyResultsTimelineActivity;
import in.sportscafe.scgame.module.user.badges.BadgeActivity;
import in.sportscafe.scgame.module.user.group.allgroups.AllGroupsActivity;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.scgame.module.user.myprofile.myposition.challenges.ChallengesFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.groups.GroupsFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.sports.SportsFragment;
import in.sportscafe.scgame.module.user.powerups.PowerUpActivity;
import in.sportscafe.scgame.module.user.sportselection.SportSelectionActivity;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileFragment extends ScGameFragment implements ProfileView, View.OnClickListener {

    private static final int SPORTS_SELECTION_CODE = 34;

    private static final int EDIT_PROFILE_CODE = 35;

    private static final int CODE_NEW_GROUP = 24;

    private static final int GROUPS_CODE = 20;

    private static final int GROUP_INFO_CODE = 10;

    private ProfilePresenter mProfilePresenter;

    private OnHomeActionListener mHomeActionListener;

    private ViewPager mViewPager;

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
        findViewById(R.id.profile_groups_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_points_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_powerups_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_sports_followed_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_badge_parent).setOnClickListener(this);
        findViewById(R.id.profile_iv_image).setOnClickListener(this);
    }

    @Override
    public void setName(String name) {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Medium.ttf");
        TextView tvName=(TextView) findViewById(R.id.profile_tv_title);
        tvName.setText(name);
        tvName.setTypeface(tftitle);
    }

    @Override
    public void setProfileImage(String imageUrl) {
        ((RoundImage) findViewById(R.id.profile_iv_image)).setImageUrl(
                imageUrl,
                Volley.getInstance().getImageLoader(),
                false
        );
    }

    @Override
    public void setSportsFollowedCount(int sportsFollowedCount) {
        TextView tvsports=(TextView) findViewById(R.id.profile_tv_sports_folld);
        tvsports.setText(String.valueOf(sportsFollowedCount));
    }

    @Override
    public void setGroupsCount(int GroupsCount) {
        TextView tvGroup=(TextView) findViewById(R.id.profile_number_of_groups);
        tvGroup.setText(String.valueOf(GroupsCount));
    }


    @Override
    public void setPowerUpsCount(int PowerUpsCount) {
        TextView tvPowerUp=(TextView) findViewById(R.id.profile_tv_powerups);
        tvPowerUp.setText(String.valueOf(PowerUpsCount));
    }


    @Override
    public void setPoints(long points) {
        TextView tvPoints=(TextView) findViewById(R.id.profile_tv_points);
        tvPoints.setText(String.valueOf(points));
    }

    @Override
    public void setBadgesCount(int badgesCount) {

        TextView tvBadges=(TextView) findViewById(R.id.profile_tv_badges);
        if (badgesCount==0){
            tvBadges.setText("0");
        }
        tvBadges.setText(String.valueOf(badgesCount));

    }


    @Override
    public void initMyPosition(LbSummary lbSummary) {
        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setAdapter(getAdapter(lbSummary));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ScGameAnalytics.getInstance().trackUserProfile(AnalyticsActions.TABS,
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
            case R.id.profile_ll_sports_followed_parent:
                navigateToSportSelection();
                break;
            case R.id.profile_btn_edit:
                navigateToEditProfile();
                break;
            case R.id.profile_iv_image:
                navigateToEditProfile();
                break;
            case R.id.profile_groups_parent:
                navigateToNewGroup();
                break;
            case R.id.profile_ll_powerups_parent:
                navigateToPowerUpScreen();
                break;
            case R.id.profile_ll_badge_parent:
                navigateToBadgeScreen();
                break;
            case R.id.profile_btn_logout:
                navigateToSettings();
                break;
        }
    }

    private void navigateToMyResults() {
        startActivity(new Intent(getContext(), MyResultsTimelineActivity.class));
        getActivity().finish();
    }

    private void navigateToSettings() {
        startActivity(new Intent(getContext(), Settings.class));
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

}