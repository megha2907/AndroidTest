package in.sportscafe.scgame.module.user.myprofile.edit;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
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
    public void updateProfile(String name, String about) {
        if(name.isEmpty()) {
            mEditProfileListener.onNameEmpty();
            return;
        }

        if(ScGame.getInstance().hasNetworkConnection()) {
            mEditProfileListener.onUpdating();
            callUpdateUserApi(name);
        } else {
            mEditProfileListener.onNoInternet();
        }
    }

    private void callUpdateUserApi(final String name) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserId(mUserInfo.getId() + "");
        updateUserRequest.setUserName(name);
        updateUserRequest.setUserPhoto(mUserInfo.getPhoto());

        MyWebService.getInstance().getUpdateUserRequest(updateUserRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            mUserInfo.setUserName(name);
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

        void onNoInternet();
    }
}