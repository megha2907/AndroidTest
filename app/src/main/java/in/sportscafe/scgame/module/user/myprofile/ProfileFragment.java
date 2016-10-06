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
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.AbstractTabFragment;
import in.sportscafe.scgame.module.common.CustomViewPager;
import in.sportscafe.scgame.module.common.RoundImage;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.common.Settings;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.module.play.myresults.MyResultsActivity;
import in.sportscafe.scgame.module.play.myresultstimeline.MyResultsTimelineActivity;
import in.sportscafe.scgame.module.user.badges.BadgeActivity;
import in.sportscafe.scgame.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.scgame.module.user.myprofile.myposition.ChallengesFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.MyGlobalFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.MyLeaguesFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.MyPositionFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.scgame.module.user.powerups.PowerUpActivity;
import in.sportscafe.scgame.module.user.sportselection.SportSelectionActivity;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileFragment extends ScGameFragment implements ProfileView, View.OnClickListener {

    private static final int SPORTS_SELECTION_CODE = 34;

    private static final int EDIT_PROFILE_CODE = 35;

    private static final int CODE_NEW_GROUP = 24;

    private ProfilePresenter mProfilePresenter;

    private OnHomeActionListener mHomeActionListener;

    private Typeface tf;

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
       // findViewById(R.id.profile_btn_back).setOnClickListener(this);
        findViewById(R.id.profile_btn_edit).setOnClickListener(this);
        findViewById(R.id.profile_btn_logout).setOnClickListener(this);
        findViewById(R.id.profile_groups_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_points_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_powerups_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_sports_followed_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_badge_parent).setOnClickListener(this);
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
    public void initMyPosition(LbSummary lbSummary) {
        CustomViewPager mMostViewedPager = (CustomViewPager) findViewById(R.id.tab_vp);
        setupViewPager(mMostViewedPager, lbSummary);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(mMostViewedPager);/*
        getChildFragmentManager().beginTransaction().replace(R.id.profile_fl_my_positions,
                MyPositionFragment.newInstance(lbSummary)).commit();*/
    }

    private void setupViewPager(ViewPager viewPager, LbSummary lbSummary) {
        viewPager.setAdapter(getAdapter(lbSummary));
    }

    private ViewPagerAdapter getAdapter(LbSummary lbSummary) {

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(MyLeaguesFragment.newInstance(lbSummary.getGroups()), "My Groups");
        pagerAdapter.addFragment(MyGlobalFragment.newInstance(lbSummary.getGlobal()), "Sports");
        pagerAdapter.addFragment(ChallengesFragment.newInstance(lbSummary.getGlobal()), "Challenges");
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
//            case R.id.profile_btn_back:
//                mHomeActionListener.onClickBack();
//                break;
            case R.id.profile_btn_edit:
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
        startActivityForResult(new Intent(getContext(), EditProfileActivity.class), EDIT_PROFILE_CODE);
    }

    private void navigateToNewGroup() {
        Intent intent = new Intent(getContext(), JoinGroupActivity.class);
        startActivityForResult(intent, CODE_NEW_GROUP);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK == resultCode) {
            if(SPORTS_SELECTION_CODE == requestCode) {
                mProfilePresenter.onGetSportsSelectionResult();
            } else if(EDIT_PROFILE_CODE == requestCode) {
                mProfilePresenter.onEditProfileDone();
            } else {
                mProfilePresenter.onGroupDetailsUpdated();
            }
        }
    }
}