package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfilePresenterImpl implements EditProfilePresenter, EditProfileModelImpl.OnEditProfileListener {

    private static final int ADD_PHOTO_REQUEST_CODE = 23;

    private EditProfileView mEditProfileView;

    private EditProfileModel mEditProfileModel;

    private boolean successfulReferral = false;

    private String screen;

    private EditProfilePresenterImpl(EditProfileView editProfileView) {
        this.mEditProfileView = editProfileView;
        this.mEditProfileModel = EditProfileModelImpl.newInstance(this);
    }

    public static EditProfilePresenter newInstance(EditProfileView editProfileView) {
        return new EditProfilePresenterImpl(editProfileView);
    }

    @Override
    public void onCreateEditProfile(Bundle bundle) {

        screen = bundle.getString("screen");
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();

        if (screen.equals(BundleKeys.HOME_SCREEN)) {
            mEditProfileView.changeViewforProfile();
        } else {
            if (userInfo != null) {
                mEditProfileView.changeViewforLogin(userInfo.getUserName());
            }
        }

        if (userInfo != null) {
            mEditProfileView.setProfileImage(userInfo.getPhoto());
            if (userInfo.getUserNickName() != null) {
                mEditProfileView.setNickName(userInfo.getUserNickName());
            }
        }
    }

    @Override
    public void onClickDone(String nickname, String referralCode, Boolean disclaimerAccepted) {
        if (nickname.equals("")) {
            mEditProfileView.setNicknameEmpty();
        } else {
            mEditProfileModel.updateProfile(nickname, referralCode, disclaimerAccepted);
        }

        NostragamusAnalytics.getInstance().trackEditProfile(AnalyticsActions.OTHERS, AnalyticsLabels.UPDATE);
    }

    @Override
    public void onClickImage() {
        mEditProfileView.navigateToAddPhoto(ADD_PHOTO_REQUEST_CODE);
    }

    @Override
    public void onGetResult(int requestCode, int resultCode, Intent data) {
        if (ADD_PHOTO_REQUEST_CODE == requestCode) {
            mEditProfileModel.onGetImage(data);
        }
    }

    @Override
    public void verifyReferralCode(String referralCode) {
        mEditProfileView.startProgressAnim();
        mEditProfileModel.callVerifyReferralCodeApi(referralCode);
    }

    @Override
    public void onUpdating() {
        mEditProfileView.startProgressAnim();
    }

    @Override
    public void onEditSuccess(boolean referralCodeExists) {

        mEditProfileView.stopProgressAnim();

        if (BuildConfig.IS_PAID_VERSION) {

            if (referralCodeExists || NostragamusDataHandler.getInstance().isMarketingCampaign()) {
                successfulReferral = true;
                mEditProfileView.navigateToOTPVerification(successfulReferral);
            } else {
                successfulReferral = false;
                mEditProfileView.navigateToOTPVerification(successfulReferral);
            }

        } else {

            if (screen.equals(BundleKeys.HOME_SCREEN)) {
                mEditProfileView.navigateToHome(true);
            } else {
                mEditProfileView.navigateToHome(false);
            }
        }

    }

    @Override
    public void onPhotoUpdate() {
        UserInfo userInfo = mEditProfileModel.getUserInfo();
        if (userInfo!=null) {
            mEditProfileView.setProfileImage(userInfo.getPhoto());
            mEditProfileView.stopProgressAnim();
        }
    }

    @Override
    public void onEditFailed(String message) {
        mEditProfileView.stopProgressAnim();
        mEditProfileView.showMessage(message);
    }

    @Override
    public void onNameEmpty() {
        mEditProfileView.showMessage(Constants.Alerts.NAME_EMPTY);
    }

    @Override
    public void onProfileImagePathNull() {
        mEditProfileView.showMessage(Constants.Alerts.IMAGE_FILEPATH_EMPTY);
    }

    @Override
    public void onNickNameEmpty() {
        mEditProfileView.showMessage(Constants.Alerts.NICKNAME_EMPTY);
    }

    @Override
    public void onNoInternet() {
        mEditProfileView.stopProgressAnim();
        mEditProfileView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onUserNameConflict() {
        mEditProfileView.stopProgressAnim();
        mEditProfileView.setNicknameConflict();
    }

    @Override
    public void onNickNameValidation() {
        mEditProfileView.setNicknameNotValid();
    }

    @Override
    public void onReferralCodeVerified() {
        mEditProfileView.stopProgressAnim();
        mEditProfileView.onCorrectReferralCode();
    }

    @Override
    public void onReferralCodeFailed(String message) {
        mEditProfileView.stopProgressAnim();
        mEditProfileView.onIncorrectReferralCode();
    }
}