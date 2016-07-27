package in.sportscafe.scgame.module.user.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.module.play.myresults.MyResultsActivity;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.scgame.module.user.myprofile.myposition.MyPositionFragment;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.scgame.module.user.sportselection.SportSelectionActivity;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileFragment extends ScGameFragment implements ProfileView, View.OnClickListener {

    private static final int SPORTS_SELECTION_CODE = 34;

    private static final int EDIT_PROFILE_CODE = 35;

    private ProfilePresenter mProfilePresenter;

    private OnHomeActionListener mHomeActionListener;

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
        findViewById(R.id.profile_btn_back).setOnClickListener(this);
        findViewById(R.id.profile_btn_edit).setOnClickListener(this);
        findViewById(R.id.profile_btn_logout).setOnClickListener(this);
        findViewById(R.id.profile_ll_points_parent).setOnClickListener(this);
        findViewById(R.id.profile_ll_sports_followed_parent).setOnClickListener(this);
    }

    @Override
    public void setName(String name) {
        ((TextView) findViewById(R.id.profile_tv_title)).setText(name);
    }

    @Override
    public void setProfileImage(String imageUrl) {
        ((HmImageView) findViewById(R.id.profile_iv_image)).setImageUrl(
                imageUrl,
                Volley.getInstance().getImageLoader(),
                false
        );
    }

    @Override
    public void setSportsFollowedCount(int sportsFollowedCount) {
        ((TextView) findViewById(R.id.profile_tv_sports_folld)).setText(String.valueOf(sportsFollowedCount));
    }

    @Override
    public void setPoints(long points) {
        ((TextView) findViewById(R.id.profile_tv_points)).setText(String.valueOf(points));
    }

    @Override
    public void initMyPosition(LbSummary lbSummary) {
        getChildFragmentManager().beginTransaction().replace(R.id.profile_fl_my_positions,
                MyPositionFragment.newInstance(lbSummary)).commit();
    }

    @Override
    public void navigateToLogIn() {
        Intent intent = new Intent(getContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            case R.id.profile_btn_back:
                mHomeActionListener.onClickBack();
                break;
            case R.id.profile_btn_edit:
                navigateToEditProfile();
                break;
            case R.id.profile_btn_logout:
                mProfilePresenter.onClickLogout();
                break;
        }
    }

    private void navigateToMyResults() {
        startActivity(new Intent(getContext(), MyResultsActivity.class));
    }

    private void navigateToSportSelection() {
        Intent intent = new Intent(getContext(), SportSelectionActivity.class);
        intent.putExtra(Constants.BundleKeys.FROM_PROFILE, true);
        startActivityForResult(intent, SPORTS_SELECTION_CODE);
    }

    private void navigateToEditProfile() {
        startActivityForResult(new Intent(getContext(), EditProfileActivity.class), EDIT_PROFILE_CODE);
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