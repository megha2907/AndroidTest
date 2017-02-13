package in.sportscafe.nostragamus.module.user.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.home.OnHomeActionListener;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.settings.SettingActivity;
import in.sportscafe.nostragamus.module.user.badges.BadgeFragment;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.powerups.PowerUpActivity;
import in.sportscafe.nostragamus.module.user.powerups.PowerUpFragment;
import in.sportscafe.nostragamus.module.user.sportselection.SportSelectionActivity;
import in.sportscafe.nostragamus.module.user.sportselection.profilesportselection.ProfileSportSelectionFragment;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileFragment extends NostragamusFragment implements ProfileView, ProfileSportSelectionFragment.OnSportSelectionChangedListener, View.OnClickListener {

    private static final int SPORTS_SELECTION_CODE = 34;

    private static final int EDIT_PROFILE_CODE = 35;

    private static final int CODE_NEW_GROUP = 24;

    private static final int GROUPS_CODE = 20;

    private static final int GROUP_INFO_CODE = 10;

    private ProfilePresenter mProfilePresenter;

    private OnHomeActionListener mHomeActionListener;

    private ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;

    private Boolean mGroupClicked = false;

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

        if(null == savedInstanceState) {
            setClickListeners();

            this.mProfilePresenter = ProfilePresenterImpl.newInstance(this);
            this.mProfilePresenter.onCreateProfile();
        }
    }


    private void setClickListeners() {
        findViewById(R.id.profile_btn_edit).setOnClickListener(this);
        findViewById(R.id.profile_btn_logout).setOnClickListener(this);
        findViewById(R.id.profile_iv_image).setOnClickListener(this);
    }

    @Override
    public void setName(String name) {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        TextView tvName = (TextView) findViewById(R.id.profile_tv_title);
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
    public void setLevel(String level) {
        ((TextView) findViewById(R.id.profile_tv_level)).setText("Level "+ level);
    }

    @Override
    public void setAccuracy(int accuracy) {
        ((TextView) findViewById(R.id.profile_btn_accuracy)).setText(accuracy + "%");
    }

    @Override
    public void setPoints(Long points) {
        ((TextView) findViewById(R.id.profile_tv_points)).setText(String.valueOf(points));
    }

    @Override
    public void setPredictionCount(Integer predictionCount) {
        ((TextView) findViewById(R.id.profile_btn_predictions)).setText(String.valueOf(predictionCount));
    }

    @Override
    public void initMyPosition(int totalMatchesPlayed, int badgesCount, int sportsFollowedCount, int powerupsCount) {
        mViewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        mViewPager.setOffscreenPageLimit(5);
        mPagerAdapter = getAdapter(totalMatchesPlayed, badgesCount, sportsFollowedCount, powerupsCount);
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                NostragamusAnalytics.getInstance().trackMyProfile(
                        mViewPager.getAdapter().getPageTitle(position).toString()
                );
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
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

    private ViewPagerAdapter getAdapter(int totalMatchesPlayed, int badgesCount, int sportsFollowedCount, int powerupsCount) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(TimelineFragment.newInstance(), formatIfPlural(totalMatchesPlayed, "Match", "es"));
        pagerAdapter.addFragment(BadgeFragment.newInstance(), formatIfPlural(badgesCount, "Achievement", "s"));
        pagerAdapter.addFragment(PowerUpFragment.newInstance(), formatIfPlural(powerupsCount, "Powerup", "s"));
        pagerAdapter.addFragment(ProfileSportSelectionFragment.newInstance(this), formatIfPlural(sportsFollowedCount, "Sport", "s"));
        return pagerAdapter;
    }

    private String formatIfPlural(int number, String label, String pluralTerm) {
        return number + "\n" + label + (number > 1 ? pluralTerm : "");
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_btn_edit:
                navigateToEditProfile();
                break;
            case R.id.profile_iv_image:
                navigateToEditProfile();
                break;
            case R.id.profile_btn_logout:
                navigateToSettings();
                break;
        }
    }

    private void navigateToSettings() {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }

    private void navigateToSportSelection() {
        Intent intent = new Intent(getContext(), SportSelectionActivity.class);
        intent.putExtra(Constants.BundleKeys.FROM_PROFILE, true);
        startActivityForResult(intent, SPORTS_SELECTION_CODE);
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("screen", Constants.BundleKeys.HOME_SCREEN);
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
        if (Activity.RESULT_OK == resultCode) {
            if (SPORTS_SELECTION_CODE == requestCode) {
                mProfilePresenter.onGetSportsSelectionResult();
            }
            if (GROUPS_CODE == requestCode) {
                mProfilePresenter.onGetUpdatedNumberofGroups();
                mProfilePresenter.onCreateProfile();
            } else if (EDIT_PROFILE_CODE == requestCode) {
                mProfilePresenter.onEditProfileDone();
            } else {
                mProfilePresenter.onGroupDetailsUpdated();
            }
        }
    }

    @Override
    public void setSportsCount(int sportsCount) {
        mPagerAdapter.updateTitle(3, formatIfPlural(sportsCount, "Sport", "s"));
    }

}