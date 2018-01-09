package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;
import android.text.TextUtils;
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

    private boolean mDisclaimerAccepted;

    private String mReferralCode;

    private boolean referralCodeExists = false;

    private EditProfileModelImpl(OnEditProfileListener listener) {
        this.mEditProfileListener = listener;
        this.mUserInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
    }

    public static EditProfileModel newInstance(OnEditProfileListener listener) {
        return new EditProfileModelImpl(listener);
    }

    @Override
    public void updateProfile(String nickname, String referralCode, Boolean disclaimerAccepted) {

        mDisclaimerAccepted = disclaimerAccepted;
        mReferralCode = referralCode;

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

                            if (response.body() != null) {
                                if (mUserInfo!=null) {
                                    mUserInfo.setPhoto(response.body().getResult());
                                    Nostragamus.getInstance().getServerDataManager().setUserInfo(mUserInfo);
                                }
                                mEditProfileListener.onPhotoUpdate();
                            }

                        } else {
                            mEditProfileListener.onEditFailed(response.message());
                        }
                    }

                });
    }

    private void callUpdateUserApi(final String nickname) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        if (mUserInfo != null) {
            updateUserRequest.setUserPhoto(mUserInfo.getPhoto());
        }
        updateUserRequest.setUserNickName(nickname);
        updateUserRequest.setDisclaimerAccepted(mDisclaimerAccepted);

        if (!TextUtils.isEmpty(mReferralCode)) {
            referralCodeExists = true;
            updateUserRequest.setReferralCode(mReferralCode);
        } else {
            updateUserRequest.setReferralCode(null);
        }

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
                                Nostragamus.getInstance().getServerDataManager().setUserInfo(mUserInfo);

                                NostragamusDataHandler.getInstance().setFirstTimeUser(false);
                                NostragamusDataHandler.getInstance().setIsProfileDisclaimerAccepted(true);

                                mEditProfileListener.onEditSuccess(referralCodeExists);
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

    @Override
    public void callVerifyReferralCodeApi(String referralCode) {

        MyWebService.getInstance().verifyReferralCodeRequest(referralCode).enqueue(
                new NostragamusCallBack<VerifyReferralCodeResponse>() {
                    @Override
                    public void onResponse(Call<VerifyReferralCodeResponse> call, Response<VerifyReferralCodeResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            if (response != null && response.isSuccessful() && response.body() != null) {
                                VerifyUserInfo verifyUserInfo = response.body().getVerifyUserInfo();

                                if (TextUtils.isEmpty(NostragamusDataHandler.getInstance().getUserReferralName())) {
                                    NostragamusDataHandler.getInstance().setUserReferralName(verifyUserInfo.getUserName());
                                }

                                if (NostragamusDataHandler.getInstance().getWalletInitialAmount() == -1 ||
                                        NostragamusDataHandler.getInstance().getWalletInitialAmount() == 0) {
                                    NostragamusDataHandler.getInstance().setWalletInitialAmount(verifyUserInfo.getWalletInitialAmount());
                                }

                                mEditProfileListener.onReferralCodeVerified();

                            } else {
                                mEditProfileListener.onReferralCodeFailed(response.message());
                            }
                        } else {
                            mEditProfileListener.onReferralCodeFailed(response.message());
                        }
                    }
                }
        );

    }

    public interface OnEditProfileListener {

        void onUpdating();

        void onEditSuccess(boolean referralCodeExists);

        void onPhotoUpdate();

        void onEditFailed(String message);

        void onNameEmpty();

        void onProfileImagePathNull();

        void onNickNameEmpty();

        void onNoInternet();

        void onUserNameConflict();

        void onNickNameValidation();

        void onReferralCodeVerified();

        void onReferralCodeFailed(String message);
    }
}