package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;
import android.os.Bundle;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.preference.PreferenceManager;
import in.sportscafe.nostragamus.module.user.preference.SavePreferenceModelImpl;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfilePresenterImpl implements EditProfilePresenter, EditProfileModelImpl.OnEditProfileListener {

    private static final int ADD_PHOTO_REQUEST_CODE = 23;

    private EditProfileView mEditProfileView;

    private EditProfileModel mEditProfileModel;

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

        if (screen.equals(Constants.BundleKeys.HOME_SCREEN)) {
            mEditProfileView.changeViewforProfile();
        } else {
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
    public void onClickImage() {
        mEditProfileView.navigateToAddPhoto(ADD_PHOTO_REQUEST_CODE);
    }

    @Override
    public void onGetResult(int requestCode, int resultCode, Intent data) {
        if (ADD_PHOTO_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
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

        if (screen.equals(Constants.BundleKeys.HOME_SCREEN)) {
            mEditProfileView.navigateToHome(true);
            //mEditProfileView.setSuccessResult();
        } else {

            mEditProfileView.navigateToHome(false);

            //mEditProfileView.navigateToSportsSelection();

            //autoSaveAllSports();

            // For ISB
            // autoSaveIsb();
        }
    }

    private void autoSaveIsb() {
        new PreferenceManager().savePreference(Arrays.asList(new Integer[]{10}),
                new SavePreferenceModelImpl.SavePreferenceModelListener() {
                    @Override
                    public void onSuccess() {
                        mEditProfileView.navigateToHome(false);
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

        List<Sport> newSportList = NostragamusDataHandler.getInstance().getAllSports();

        List<Integer> sportIdList = new ArrayList<Integer>();

        for (Sport sport : newSportList) {
            sportIdList.add(sport.getId());
        }

        new PreferenceManager().savePreference(sportIdList,
                new SavePreferenceModelImpl.SavePreferenceModelListener() {
                    @Override
                    public void onSuccess() {
                        mEditProfileView.navigateToHome(false);
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
        UserInfo userInfo = mEditProfileModel.getUserInfo();
        mEditProfileView.setProfileImage(userInfo.getPhoto());

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