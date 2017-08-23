package in.sportscafe.nostragamus.module.user.login;

import com.jeeva.android.Log;

import java.util.HashMap;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserLoginInResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 1/10/17.
 */

public class UserInfoModelImpl {

    private static final String TAG = UserInfoModelImpl.class.getSimpleName();
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
        } else {
            mUserInfoModelListener.onNoInternet();
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
                            Nostragamus.getInstance().getServerDataManager().setUserInfo(userInfo);

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
            performLogoutIfRequired(userInfo);

            NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();
            nostragamusDataHandler.setUserInfo(userInfo);

            HashMap<String, Integer> powerUpMap = userInfo.getPowerUps();

            nostragamusDataHandler.set2xPowerupsCount(powerUpMap.get(Powerups.XX));
            nostragamusDataHandler.setNonegsPowerupsCount(powerUpMap.get(Powerups.NO_NEGATIVE));
            nostragamusDataHandler.setPollPowerupsCount(powerUpMap.get(Powerups.AUDIENCE_POLL));
            nostragamusDataHandler.setReplayPowerupsCount(powerUpMap.get(Powerups.MATCH_REPLAY));
            nostragamusDataHandler.setFlipPowerupsCount(powerUpMap.get(Powerups.ANSWER_FLIP));

            nostragamusDataHandler.setPowerUpsUsedCount(userInfo.getPowerupsUsedCount());
            nostragamusDataHandler.setMatchPlayedCount(userInfo.getTotalMatchesPlayed());
            nostragamusDataHandler.setTotalGroupsCount(userInfo.getTotalGroups());

        }
    }

    /**
     * If Pro version && wallet NOT created, make logout
     */
    private void performLogoutIfRequired(UserInfo userInfo) {
        if (BuildConfig.IS_PAID_VERSION) {
            if (userInfo != null && userInfo.getInfoDetails() != null) {

                if (!userInfo.getInfoDetails().isWalletCreated()) {
                    Log.d(TAG, "Logging out as wallet not created");
                    Nostragamus.getInstance().logout();
                }
            }
        }
    }

    public interface OnGetUserInfoModelListener {

        void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo);

        void onFailedGetUpdateUserInfo(String message);

        void onNoInternet();
    }
}