package in.sportscafe.nostragamus.module.user.login;

import com.jeeva.android.Log;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 1/10/17.
 */

public class UserInfoModelImpl {

    private UserInfoModelImpl.OnGetUserInfoModelListener mUserInfoModelListener;

    private UserInfoModelImpl(OnGetUserInfoModelListener listener) {
        this.mUserInfoModelListener = listener;
    }

    public static UserInfoModelImpl newInstance(OnGetUserInfoModelListener listener) {
        return new UserInfoModelImpl(listener);
    }

    public void getUserInfo() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callUserInfoApi();
        }
    }

    private void callUserInfoApi() {
        MyWebService.getInstance().getUserInfoRequest(NostragamusDataHandler.getInstance().getUserId()).enqueue(
                new NostragamusCallBack<UserInfoResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful()) {
                            UserInfo userInfo = response.body().getUserInfo();
                            handleUserInfoResponse(userInfo);
                            mUserInfoModelListener.onSuccessGetUpdatedUserInfo(userInfo);
                        } else {
                            mUserInfoModelListener.onFailedGetUpdateUserInfo(response.message());
                        }
                    }
                }
        );
    }

    public void handleUserInfoResponse(UserInfo userInfo) {
        if (null != userInfo) {
            NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();
            nostragamusDataHandler.setUserInfo(userInfo);

            HashMap<String, Integer> powerUpMap = userInfo.getPowerUps();
            nostragamusDataHandler.setNumberof2xGlobalPowerups(powerUpMap.get(Powerups.XX_GLOBAL));

            nostragamusDataHandler.setNumberof2xPowerups(powerUpMap.get(Powerups.XX));
            nostragamusDataHandler.setNumberofNonegsPowerups(powerUpMap.get(Powerups.NO_NEGATIVE));
            nostragamusDataHandler.setNumberofAudiencePollPowerups(powerUpMap.get(Powerups.AUDIENCE_POLL));
            nostragamusDataHandler.setNumberofReplayPowerups(powerUpMap.get(Powerups.MATCH_REPLAY));
            nostragamusDataHandler.setNumberofFlipPowerups(powerUpMap.get(Powerups.ANSWER_FLIP));
        }
    }

    public interface OnGetUserInfoModelListener {

        void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo);

        void onFailedGetUpdateUserInfo(String message);
    }
}