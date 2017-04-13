package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.UUID;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfileModelImpl implements EditProfileModel {

    private UserInfo mUserInfo;

    private OnEditProfileListener mEditProfileListener;

    private EditProfileModelImpl(OnEditProfileListener listener) {
        this.mEditProfileListener = listener;
        this.mUserInfo = NostragamusDataHandler.getInstance().getUserInfo();
    }

    public static EditProfileModel newInstance(OnEditProfileListener listener) {
        return new EditProfileModelImpl(listener);
    }

    @Override
    public void updateProfile(String nickname) {
        if (nickname.isEmpty()) {
            mEditProfileListener.onNickNameEmpty();
            return;
        } else if (nickname.length() < 3 || nickname.length() > 15) {
            mEditProfileListener.onNickNameValidation();
            return;
        }

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            mEditProfileListener.onUpdating();
            callUpdateUserApi(nickname);
        } else {
            mEditProfileListener.onNoInternet();
        }
    }

    @Override
    public void onGetImage(Intent imageData) {
        String imagePath = imageData.getStringExtra(BundleKeys.ADDED_NEW_IMAGE_PATH);
        Log.i("file", imagePath);

        File file = new File(imagePath);
        updateProfilePhoto(file, "game/profileimage/", file.getName());
    }

    @Override
    public void updateProfilePhoto(File file, String filepath, String filename) {
        if (filepath.equals(null)) {
            mEditProfileListener.onProfileImagePathNull();
            return;
        }
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            mEditProfileListener.onUpdating();
            callUpdateUserProfilePhotoApi(file, filepath, UUID.randomUUID().toString() + "_" + filename);
        } else {
            mEditProfileListener.onNoInternet();
        }
    }

    private void callUpdateUserProfilePhotoApi(File file, String filepath, String filename) {
        MyWebService.getInstance().getUploadPhotoRequest(file, filepath, filename)
                .enqueue(new NostragamusCallBack<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful()) {
                            mUserInfo.setPhoto(response.body().getResult());
                            NostragamusDataHandler.getInstance().setUserInfo(mUserInfo);
                            callUpdateUserApi(NostragamusDataHandler.getInstance().getUserInfo().getUserNickName());

                            mEditProfileListener.onPhotoUpdate();

                        } else {
                            mEditProfileListener.onEditFailed(response.message());
                        }
                    }

                });
    }

    private void callUpdateUserApi(final String nickname) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserPhoto(mUserInfo.getPhoto());
        updateUserRequest.setUserNickName(nickname);

        MyWebService.getInstance().getUpdateUserRequest(updateUserRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {

                            if (response.body().getMessage().equals("user_nick conflict")) {
                                mEditProfileListener.onUserNameConflict();
                            } else {
                                mUserInfo.setUserNickName(nickname);
                                NostragamusDataHandler.getInstance().setUserInfo(mUserInfo);

                                NostragamusDataHandler.getInstance().setFirstTimeUser(false);
                                NostragamusDataHandler.getInstance().setIsProfileDisclaimerAccepted(true);

                                mEditProfileListener.onEditSuccess();
                            }
                        } else {
                            mEditProfileListener.onEditFailed(response.message());
                        }
                    }
                }
        );
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public interface OnEditProfileListener {

        void onUpdating();

        void onEditSuccess();

        void onPhotoUpdate();

        void onEditFailed(String message);

        void onNameEmpty();

        void onProfileImagePathNull();

        void onNickNameEmpty();

        void onNoInternet();

        void onUserNameConflict();

        void onNickNameValidation();
    }
}