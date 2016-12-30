package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.os.Bundle;

import com.jeeva.android.Log;

import java.io.File;
import java.util.Arrays;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.preference.PreferenceManager;
import in.sportscafe.nostragamus.module.user.preference.SavePreferenceModelImpl;

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

        screen = bundle.getString("screen");

        if (screen.equals(Constants.BundleKeys.HOME_SCREEN))
        {
            mEditProfileView.changeViewforProfile();
        }
        else {
            mEditProfileView.changeViewforLogin(NostragamusDataHandler.getInstance().getUserInfo().getUserName());
        }

        UserInfo userInfo = mEditProfileModel.getUserInfo();
        mEditProfileView.setProfileImage(userInfo.getPhoto());
        if (userInfo.getUserNickName() != null) {
            mEditProfileView.setNickName(userInfo.getUserNickName());
        }
    }

    @Override
    public void onClickDone(String nickname) {
        if (nickname.equals("")) {
            mEditProfileView.setNicknameEmpty();
        } else {
            mEditProfileModel.updateProfile(nickname);
        }

        NostragamusAnalytics.getInstance().trackEditProfile(AnalyticsActions.OTHERS, AnalyticsLabels.UPDATE);
    }

    @Override
    public void onProfilePhotoDone(File file, String filepath, String filename) {
        mEditProfileModel.updateProfilePhoto(file, filepath,filename);
    }

    @Override
    public void onUpdating() {
        mEditProfileView.showProgressbar();
    }

    @Override
    public void onEditSuccess() {
        mEditProfileView.dismissProgressbar();

        if (screen.equals(Constants.BundleKeys.HOME_SCREEN))
        {
            //mEditProfileView.navigateToHome();
            mEditProfileView.setSuccessResult();
        }
        else {
          //mEditProfileView.navigateToSportsSelection();

            autoSaveAllSports();

            // For ISB
           // autoSaveIsb();
        }
    }

    private void autoSaveIsb() {
        new PreferenceManager().savePreference(Arrays.asList(new Integer[] {10}),
                new SavePreferenceModelImpl.SavePreferenceModelListener() {
            @Override
            public void onSuccess()
            {
                mEditProfileView.navigateToHome();
            }

            @Override
            public void onNoInternet() {
                onNoInternet();
            }

            @Override
            public void onFailed(String message) {
                onEditFailed(message);
            }
        });
    }


    private void autoSaveAllSports() {
        new PreferenceManager().savePreference(Arrays.asList(new Integer[] {1,2,3,4,5,6,7,8,9,10}),
                new SavePreferenceModelImpl.SavePreferenceModelListener() {
                    @Override
                    public void onSuccess()
                    {
                        Log.i("selected","inside");
                        mEditProfileView.navigateToHome();
                    }

                    @Override
                    public void onNoInternet() {
                        onNoInternet();
                    }

                    @Override
                    public void onFailed(String message) {
                        onEditFailed(message);
                    }
                });
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

    @Override
    public void onNickNameValidation() {
        mEditProfileView.setNicknameNotValid();
    }
}