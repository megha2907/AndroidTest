package in.sportscafe.scgame.module.user.myprofile.edit;

import android.os.Bundle;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfilePresenterImpl implements EditProfilePresenter, EditProfileModelImpl.OnEditProfileListener {

    private EditProfileView mEditProfileView;

    private EditProfileModel mEditProfileModel;

    private EditProfilePresenterImpl(EditProfileView editProfileView) {
        this.mEditProfileView = editProfileView;
        this.mEditProfileModel = EditProfileModelImpl.newInstance(this);
    }

    public static EditProfilePresenter newInstance(EditProfileView editProfileView) {
        return new EditProfilePresenterImpl(editProfileView);
    }

    @Override
    public void onCreateEditProfile(Bundle bundle) {
        UserInfo userInfo = mEditProfileModel.getUserInfo();

        mEditProfileView.setName(userInfo.getUserName());
        mEditProfileView.setProfileImage(userInfo.getPhoto());
    }

    @Override
    public void onClickDone(String name, String about) {
        mEditProfileModel.updateProfile(name, about);
    }

    @Override
    public void onUpdating() {
        mEditProfileView.showProgressbar();
    }

    @Override
    public void onEditSuccess() {
        mEditProfileView.dismissProgressbar();
        mEditProfileView.setSuccessResult();
        mEditProfileView.close();
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
    public void onNoInternet() {
        mEditProfileView.dismissProgressbar();
        mEditProfileView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }
}