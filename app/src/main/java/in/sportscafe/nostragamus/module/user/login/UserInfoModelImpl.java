package in.sportscafe.nostragamus.module.user.login;

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

public class UserInfoModelImpl  {

    private UserInfoModelImpl.OnGetUserInfoModelListener mUserInfoModelListener;

    private UserInfoModelImpl( OnGetUserInfoModelListener listener) {
        this.mUserInfoModelListener = listener;
    }


    public static UserInfoModelImpl newInstance(OnGetUserInfoModelListener listener) {
        return new UserInfoModelImpl(listener);
    }

    public void getUserInfo() {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
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
                            UserInfo updatedUserInfo = response.body().getUserInfo();

                            if (null != updatedUserInfo) {

                                NostragamusDataHandler.getInstance().setUserInfo(updatedUserInfo);

                                mUserInfoModelListener.onSuccessGetUpdatedUserInfo(updatedUserInfo);

                                if(null != updatedUserInfo)
                                {
                                    NostragamusDataHandler.getInstance().setNumberof2xPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                    NostragamusDataHandler.getInstance().setNumberofNonegsPowerups(updatedUserInfo.getPowerUps().get("no_negs"));
                                    NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(updatedUserInfo.getPowerUps().get("player_poll"));
                                    NostragamusDataHandler.getInstance().setNumberofReplayPowerups(updatedUserInfo.getPowerUps().get("match_replay"));
                                    NostragamusDataHandler.getInstance().setNumberofFlipPowerups(updatedUserInfo.getPowerUps().get("answer_flip"));
                                }
                                NostragamusDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());
                                NostragamusDataHandler.getInstance().setNumberofGroups(updatedUserInfo.getTotalGroups());
                            }

                        }else {

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
