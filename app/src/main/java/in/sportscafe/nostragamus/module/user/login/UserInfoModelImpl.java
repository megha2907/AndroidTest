package in.sportscafe.nostragamus.module.user.login;

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
                        if (response.isSuccessful()) {
                            super.onResponse(call, response);
                            NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();

                            UserInfo updatedUserInfo = response.body().getUserInfo();
                            if (null != updatedUserInfo) {
                                nostragamusDataHandler.setUserInfo(updatedUserInfo);

                                HashMap<String, Integer> powerUpMap = updatedUserInfo.getPowerUps();
                                nostragamusDataHandler.setNumberof2xPowerups(powerUpMap.get(Powerups.XX));
                                nostragamusDataHandler.setNumberofNonegsPowerups(powerUpMap.get(Powerups.NO_NEGATIVE));
                                nostragamusDataHandler.setNumberofAudiencePollPowerups(powerUpMap.get(Powerups.AUDIENCE_POLL));
                                nostragamusDataHandler.setNumberofReplayPowerups(powerUpMap.get(Powerups.MATCH_REPLAY));
                                nostragamusDataHandler.setNumberofFlipPowerups(powerUpMap.get(Powerups.ANSWER_FLIP));

                                nostragamusDataHandler.setNumberofBadges(updatedUserInfo.getBadges().size());
                                nostragamusDataHandler.setNumberofGroups(updatedUserInfo.getTotalGroups());

                                mUserInfoModelListener.onSuccessGetUpdatedUserInfo(updatedUserInfo);
                            }
                        } else {
                            mUserInfoModelListener.onFailedGetUpdateUserInfo(response.message());
                        }
                    }
                }
        );
    }

    public interface OnGetUserInfoModelListener {

        void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo);

        void onFailedGetUpdateUserInfo(String message);
    }
}