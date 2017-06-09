package in.sportscafe.nostragamus.module.user.myprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.animator.AppBarStateChangeListener;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineFragment;
import in.sportscafe.nostragamus.module.settings.SettingActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileFragment extends NostragamusFragment implements ProfileView, View.OnClickListener {

    private static final int EDIT_PROFILE_CODE = 35;

    private ProfilePresenter mProfilePresenter;

    private ViewPagerAdapter mPagerAdapter;

    private TimelineFragment mTimelineFragment;

    public static ProfileFragment newInstance(int tabPosition, Bundle bundle) {
        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putInt(BundleKeys.TAB_POSITION, tabPosition);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ProfileFragment newInstance(int tabPosition, boolean separateScreen, Bundle bundle) {
        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putInt(BundleKeys.TAB_POSITION, tabPosition);
        bundle.putBoolean(BundleKeys.IS_SEPARATE_SCREEN, separateScreen);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null == savedInstanceState) {
            initListeners();

            this.mProfilePresenter = ProfilePresenterImpl.newInstance(this);
            this.mProfilePresenter.onCreateProfile(getArguments());
        }
    }

    private void initListeners() {
//        findViewById(R.id.profile_btn_settings).setOnClickListener(this);
        findViewById(R.id.profile_iv_image).setOnClickListener(this);
        findViewById(R.id.profile_btn_edit).setOnClickListener(this);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (null != mTimelineFragment) {
                    if (state == State.EXPANDED) {
                        mTimelineFragment.setAppbarExpanded(false);
                    } else {
                        mTimelineFragment.setAppbarExpanded(true);
                    }
                }
            }
        });
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
        ((TextView) findViewById(R.id.profile_tv_level)).setText("Level " + level);
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
    public void updateSportTabTitle(String title) {
        // mPagerAdapter.updateTitle(3, title);
    }

    @Override
    public void setAdapter(ViewPagerAdapter adapter) {
        ViewPager viewPager = (CustomViewPager) findViewById(R.id.tab_vp);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(mPagerAdapter = adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                NostragamusAnalytics.getInstance().trackMyProfile(
                        mPagerAdapter.getPageTitle(position).toString()
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
        tabLayout.setupWithViewPager(viewPager);

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getContext().getResources().getColor(R.color.profile_tab_line_color));
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

        /*if (!BuildConfig.IS_PAID_VERSION) {
            // For free app, there are only 3 fragment-items/tab available, make them fix-sized
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }*/

        viewPager.setCurrentItem(getArguments().getInt(BundleKeys.TAB_POSITION));

        mTimelineFragment = (TimelineFragment) mPagerAdapter.getItem(0);
    }

    @Override
    public void navigateToLogIn() {
        Intent intent = new Intent(getContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void changeToSeparateScreenMode() {
        View backBtn = findViewById(R.id.profile_btn_back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

        findViewById(R.id.profile_iv_image).setEnabled(false);
//        findViewById(R.id.profile_btn_settings).setVisibility(View.INVISIBLE);
        findViewById(R.id.profile_btn_edit).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.profile_btn_settings:
                navigateToSettings();
                break;*/
            case R.id.profile_iv_image:
                navigateToEditProfile();
                break;
            case R.id.profile_btn_edit:
                navigateToEditProfile();
                break;
            case R.id.profile_btn_back:
                getActivity().onBackPressed();
                break;
        }
    }

    private void navigateToSettings() {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        intent.putExtra(BundleKeys.EDIT_PROFILE_LAUNCHED_FROM, EditProfileActivity.ILaunchedFrom.PROFILE_FRAGMENT);
        intent.putExtra("screen", BundleKeys.HOME_SCREEN);
        startActivityForResult(intent, EDIT_PROFILE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (EDIT_PROFILE_CODE == requestCode) {
                mProfilePresenter.onEditProfileDone();
            }
        }
    }

}