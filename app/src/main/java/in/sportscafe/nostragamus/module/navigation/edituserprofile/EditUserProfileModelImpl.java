package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.UUID;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.module.user.myprofile.edit.UpdateUserRequest;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 8/21/17.
 */

public class EditUserProfileModelImpl  implements EditUserProfileModel {

    private UserInfo mUserInfo;

    private EditUserProfileModelImpl.OnEditUserProfileListener mEditProfileListener;


    private EditUserProfileModelImpl(EditUserProfileModelImpl.OnEditUserProfileListener listener) {
        this.mEditProfileListener = listener;
        this.mUserInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
    }

    public static EditUserProfileModel newInstance(EditUserProfileModelImpl.OnEditUserProfileListener listener) {
        return new EditUserProfileModelImpl(listener);
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
        String imagePath = imageData.getStringExtra(Constants.BundleKeys.ADDED_NEW_IMAGE_PATH);
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
                            Nostragamus.getInstance().getServerDataManager().setUserInfo(mUserInfo);
                            //callUpdateUserApi(NostragamusDataHandler.getInstance().getUserInfo().getUserNickName());

                            mEditProfileListener.onPhotoUpdate();

                        } else {
                            mEditProfileListener.onEditFailed(response.message());
                        }
                    }

                });
    }

    private void callUpdateUserApi(final String nickname) {
        UpdateUserProfileRequest updateUserProfileRequest = new UpdateUserProfileRequest();
        updateUserProfileRequest.setUserPhoto(mUserInfo.getPhoto());
        updateUserProfileRequest.setUserNickName(nickname);

        MyWebService.getInstance().getUpdateUserProfileRequest(updateUserProfileRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {

                            if (response.body().getMessage().equals("user_nick conflict")) {
                                mEditProfileListener.onUserNameConflict();
                            } else {
                                mUserInfo.setUserNickName(nickname);
                                Nostragamus.getInstance().getServerDataManager().setUserInfo(mUserInfo);

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


    public interface OnEditUserProfileListener {

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
