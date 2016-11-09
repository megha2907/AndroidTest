package in.sportscafe.scgame.module.user.myprofile;

import android.content.Context;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;

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
        mProfileView.setSportsFollowedCount(ScGameDataHandler.getInstance().getFavoriteSportsIdList().size());
    }

    @Override
    public void onGetUpdatedNumberofGroups() {
        mProfileView.setGroupsCount(ScGameDataHandler.getInstance().getNumberofGroups());
    }

    @Override
    public void onGetGroupCount() {
        if (ScGameDataHandler.getInstance().getNumberofGroups()==0) {
            mProfileView.setGroupsCount(0);
        } else {
            mProfileView.setGroupsCount(ScGameDataHandler.getInstance().getNumberofGroups());
        }
    }

    @Override
    public void onGetPowerUpsCount() {
        mProfileView.setPowerUpsCount(ScGameDataHandler.getInstance().getNumberofPowerups());
    }

    @Override
    public void onGetBadgesCount() {
        mProfileView.setBadgesCount(ScGameDataHandler.getInstance().getNumberofBadges());
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
        ScGameDataHandler.getInstance().clearAll();
        mProfileView.navigateToLogIn();
    }

    private void getProfile() {
        mProfileView.showProgressbar();
        mProfileModel.getProfileDetails();
    }

    @Override
    public void onGetProfileSuccess(LbSummary lbSummary) {
        mProfileView.dismissProgressbar();
        mProfileView.setPoints(lbSummary.getTotalPoints());
        mProfileView.initMyPosition(lbSummary);
    }

    private void populateUserInfo() {
        UserInfo userInfo = mProfileModel.getUserInfo();
        mProfileView.setName(userInfo.getUserNickName());
        mProfileView.setProfileImage(userInfo.getPhoto());
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