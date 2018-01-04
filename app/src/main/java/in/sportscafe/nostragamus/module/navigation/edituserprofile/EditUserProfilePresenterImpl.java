package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by deepanshi on 8/22/17.
 */

public class EditUserProfilePresenterImpl implements EditUserProfilePresenter, EditUserProfileModelImpl.OnEditUserProfileListener {

    private static final int ADD_PHOTO_REQUEST_CODE = 23;

    private EditUserProfileView mEditProfileView;

    private EditUserProfileModel mEditProfileModel;


    private EditUserProfilePresenterImpl(EditUserProfileView editUserProfileView) {
        this.mEditProfileView = editUserProfileView;
        this.mEditProfileModel = EditUserProfileModelImpl.newInstance(this);
    }

    public static EditUserProfilePresenter newInstance(EditUserProfileView editUserProfileView) {
        return new EditUserProfilePresenterImpl(editUserProfileView);
    }

    @Override
    public void onCreateEditProfile() {
        UserInfo userInfo = mEditProfileModel.getUserInfo();
        if (userInfo!=null) {
            mEditProfileView.setProfileImage(userInfo.getPhoto());
            if (userInfo.getUserNickName() != null) {
                mEditProfileView.setNickName(userInfo.getUserNickName());
            }
        }
    }

    @Override
    public void onClickDone(String nickname) {
        if (nickname.equals("")) {
            mEditProfileView.setNicknameEmpty();
        } else {
            mEditProfileModel.updateProfile(nickname);
        }

        NostragamusAnalytics.getInstance().trackEditProfile(Constants.AnalyticsActions.OTHERS, Constants.AnalyticsLabels.UPDATE);
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
    public void onUpdating() {
        mEditProfileView.showProgressbar();
    }

    @Override
    public void onEditSuccess() {

        mEditProfileView.dismissProgressbar();
        mEditProfileView.navigateToHome(true);
    }

    @Override
    public void onPhotoUpdate() {
        UserInfo userInfo = mEditProfileModel.getUserInfo();
        if (userInfo!=null) {
            mEditProfileView.setProfileImage(userInfo.getPhoto());
        }
        mEditProfileView.dismissProgressbar();
    }

    @Override
    public void onEditFailed(String message) {
        mEditProfileView.dismissProgressbar();
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
        mEditProfileView.dismissProgressbar();
        mEditProfileView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onUserNameConflict() {
        mEditProfileView.dismissProgressbar();
        mEditProfileView.setNicknameConflict();
    }

    @Override
    public void onNickNameValidation() {
        mEditProfileView.setNicknameNotValid();
    }

}
