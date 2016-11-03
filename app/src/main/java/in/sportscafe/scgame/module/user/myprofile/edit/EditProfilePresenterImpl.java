package in.sportscafe.scgame.module.user.myprofile.edit;

import android.os.Bundle;
import android.util.EventLog;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfilePresenterImpl implements EditProfilePresenter, EditProfileModelImpl.OnEditProfileListener {

    private EditProfileView mEditProfileView;

    private EditProfileModel mEditProfileModel;

    private int EDIT_PROFILE_CODE;

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

        screen=bundle.getString("screen");

        UserInfo userInfo = mEditProfileModel.getUserInfo();

        mEditProfileView.setProfileImage(userInfo.getPhoto());
        if(userInfo.getUserNickName()!= null){
            mEditProfileView.setNickName(userInfo.getUserNickName());
        }

    }

    @Override
    public void onClickDone(String nickname) {

        if(nickname.equals("")){
            mEditProfileView.setNicknameEmpty();
        }
        else
        {
            mEditProfileModel.updateProfile(nickname);
        }
    }

    @Override
    public void onProfilePhotoDone(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {
        mEditProfileModel.updateProfilePhoto(file, filepath,filename);
    }

    @Override
    public void onUpdating() {
        mEditProfileView.showProgressbar();
    }

    @Override
    public void onEditSuccess() {
        mEditProfileView.dismissProgressbar();
        mEditProfileView.setSuccessResult();

        if (screen.equals(Constants.BundleKeys.HOME_SCREEN))
        {
            mEditProfileView.navigateToHome();
        }
        else {
            mEditProfileView.navigateToSportsSelection();
        }
    }

    @Override
    public void onPhotoUpdate() {
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
}