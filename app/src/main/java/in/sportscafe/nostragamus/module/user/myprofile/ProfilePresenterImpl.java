package in.sportscafe.nostragamus.module.user.myprofile;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfilePresenterImpl implements ProfilePresenter, ProfileModelImpl.OnProfileModelListener {

    private ProfileView mProfileView;

    private ProfileModel mProfileModel;

    private GroupInfo mGroupInfo;

    private ProfilePresenterImpl(ProfileView profileView) {
        this.mProfileView = profileView;
        this.mProfileModel = ProfileModelImpl.newInstance(this);
    }

    public static ProfilePresenter newInstance(ProfileView profileView) {
        return new ProfilePresenterImpl(profileView);
    }

    @Override
    public void onCreateProfile() {
        populateUserInfo();
        getProfile();
    }

    @Override
    public void onEditProfileDone() {
        updateBasicDetails();
    }

    private void getProfile() {
        mProfileModel.getProfileDetails();
    }

    @Override
    public void onGetProfileSuccess() {
        mProfileView.dismissProgressbar();
        populateUserInfo();
        updateAdapterDetails();
    }

    private void populateUserInfo() {
        UserInfo userInfo = mProfileModel.getUserInfo();
        updateBasicDetails();
        updateUserRelaventDetails(userInfo.getInfoDetails().getLevel(), userInfo.getAccuracy(), userInfo.getPredictionCount(), userInfo.getTotalPoints());
    }

    private void updateAdapterDetails() {
        mProfileView.setAdapter(mProfileModel.getAdapter(mProfileView.getChildFragmentManager()));
    }

    private void updateBasicDetails() {
        UserInfo userInfo = mProfileModel.getUserInfo();
        mProfileView.setName(userInfo.getUserNickName());
        mProfileView.setProfileImage(userInfo.getPhoto());
    }

    private void updateUserRelaventDetails(String level, int accuracy, int predictionCount, Long totalPoints) {
        if (!TextUtils.isEmpty(level)) {
            mProfileView.setLevel(level);
        } else {
            mProfileView.setLevel("1");
        }

        mProfileView.setAccuracy(accuracy);
        mProfileView.setPredictionCount(predictionCount);
        mProfileView.setPoints(totalPoints);
    }

    @Override
    public void onGetProfileFailed(String message) {
        mProfileView.dismissProgressbar();
        showAlert(message);
    }

    @Override
    public void onNoInternet() {
        mProfileView.dismissProgressbar();
        showAlert(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onSportsTitleChanged(String title) {
        mProfileView.updateSportTabTitle(title);
    }

    private void showAlert(String message) {
        mProfileView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getProfile();
                    }
                });
    }
}