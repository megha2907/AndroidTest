package in.sportscafe.nostragamus.module.user.myprofile;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.jeeva.android.Log;

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
    public void onGetSportsSelectionResult() {
        mProfileView.setSportsFollowedCount(NostragamusDataHandler.getInstance().getFavoriteSportsIdList().size());
    }

    @Override
    public void onGetUpdatedNumberofGroups() {
        mProfileView.setGroupsCount(NostragamusDataHandler.getInstance().getNumberofGroups());
    }

    @Override
    public void onGetGroupCount() {
        if (NostragamusDataHandler.getInstance().getNumberofGroups()==0) {
            mProfileView.setGroupsCount(0);
        } else {
            mProfileView.setGroupsCount(NostragamusDataHandler.getInstance().getNumberofGroups());
        }
    }

    @Override
    public void onGetPowerUpsCount() {
        mProfileView.setPowerUpsCount(NostragamusDataHandler.getInstance().getNumberof2xGlobalPowerups()+
                NostragamusDataHandler.getInstance().getNumberof2xPowerups()+
                NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups()+
                NostragamusDataHandler.getInstance().getNumberofNonegsPowerups()+
                NostragamusDataHandler.getInstance().getNumberofFlipPowerups()+
                NostragamusDataHandler.getInstance().getNumberofReplayPowerups());
    }

    @Override
    public void onGetBadgesCount() {
        mProfileView.setBadgesCount(NostragamusDataHandler.getInstance().getNumberofBadges());
    }

    @Override
    public void onEditProfileDone() {
        populateUserInfo();
    }

    @Override
    public void onGroupDetailsUpdated() {
        getProfile();
    }

    @Override
    public void onClickLogout() {
        NostragamusDataHandler.getInstance().clearAll();
        mProfileView.navigateToLogIn();
    }

    private void getProfile() {
        mProfileView.showProgressbar();
        mProfileModel.getProfileDetails();
    }

    @Override
    public void onGetProfileSuccess(UserInfo userInfo) {
        mProfileView.dismissProgressbar();
        mProfileView.setPoints(userInfo.getTotalPoints());
        mProfileView.initMyPosition(userInfo);
    }

    private void populateUserInfo() {
        UserInfo userInfo = mProfileModel.getUserInfo();
        mProfileView.setName(userInfo.getUserNickName());
        mProfileView.setProfileImage(userInfo.getPhoto());

        if (!TextUtils.isEmpty(userInfo.getInfoDetails().getLevel())) {
            mProfileView.setLevel(userInfo.getInfoDetails().getLevel());
        }else {
            mProfileView.setLevel("1");
        }

        mProfileView.setAccuracy(userInfo.getAccuracy());
        mProfileView.setPredictionCount(userInfo.getPredictionCount());

        onGetSportsSelectionResult();
        onGetGroupCount();
        onGetPowerUpsCount();
        onGetBadgesCount();
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
    public Context getContext() {
        return mProfileView.getContext();
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