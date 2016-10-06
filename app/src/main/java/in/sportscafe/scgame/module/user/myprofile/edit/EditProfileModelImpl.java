package in.sportscafe.scgame.module.user.myprofile.edit;

import android.view.Gravity;
import android.widget.Toast;

import com.jeeva.android.Log;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.Result;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        this.mUserInfo = ScGameDataHandler.getInstance().getUserInfo();
    }

    public static EditProfileModel newInstance(OnEditProfileListener listener) {
        return new EditProfileModelImpl(listener);
    }

    @Override
    public void updateProfile(String name, String nickname) {
        if(name.isEmpty()) {
            mEditProfileListener.onNameEmpty();
            return;
        }
        else if(nickname.isEmpty()){
            mEditProfileListener.onNickNameEmpty();
            return;
        }

        if(ScGame.getInstance().hasNetworkConnection()) {
            mEditProfileListener.onUpdating();
            callUpdateUserApi(name,nickname);
        } else {
            mEditProfileListener.onNoInternet();
        }
    }


    @Override
    public void updateProfilePhoto(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {
        if(filepath.equals(null)) {
            mEditProfileListener.onProfileImagePathNull();
            return;
        }
        if(ScGame.getInstance().hasNetworkConnection()) {
            mEditProfileListener.onUpdating();
            callUpdateUserProfilePhotoApi(file,filepath,filename);
        } else {
            mEditProfileListener.onNoInternet();
        }
    }

    private void callUpdateUserProfilePhotoApi(MultipartBody.Part file, RequestBody filepath, RequestBody filename) {

        MyWebService.getInstance().getUpdateUserProfilePhotoRequest(file,filepath,filename).enqueue(new ScGameCallBack<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                if (response.isSuccessful()) {
                    mUserInfo.setPhoto(response.body().getResult());
                    mEditProfileListener.onEditSuccess();
                } else {
                    mEditProfileListener.onEditFailed(response.message());
                }
            }

        });

    }

    private void callUpdateUserApi(final String name,final String nickname) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(mUserInfo.getId() + "");
        updateUserRequest.setUserName(name);
        updateUserRequest.setUserPhoto(mUserInfo.getPhoto());
        updateUserRequest.setUserNickName(nickname);

        MyWebService.getInstance().getUpdateUserRequest(updateUserRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            mUserInfo.setUserName(name);
                            mUserInfo.setUserNickName(nickname);
                            ScGameDataHandler.getInstance().setUserInfo(mUserInfo);

                            mEditProfileListener.onEditSuccess();
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

        void onEditFailed(String message);

        void onNameEmpty();

        void onProfileImagePathNull();

        void onNickNameEmpty();

        void onNoInternet();
    }
}