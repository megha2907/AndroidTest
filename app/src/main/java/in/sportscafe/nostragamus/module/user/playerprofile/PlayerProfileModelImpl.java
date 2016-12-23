package in.sportscafe.nostragamus.module.user.playerprofile;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileModel;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileModelImpl;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummaryResponse;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfoResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 12/22/16.
 */

public class PlayerProfileModelImpl implements PlayerProfileModel {

    private PlayerInfo mplayerInfo;

    private PlayerProfileModelImpl.OnProfileModelListener mProfileModelListener;

    private PlayerProfileModelImpl(PlayerProfileModelImpl.OnProfileModelListener listener) {
        this.mProfileModelListener = listener;
    }

    public static PlayerProfileModel newInstance(PlayerProfileModelImpl.OnProfileModelListener listener) {
        return new PlayerProfileModelImpl(listener);
    }

    @Override
    public void getProfileDetails(Bundle bundle) {
        String playerId = bundle.getString(Constants.BundleKeys.PLAYER_ID);
        getUserInfoFromServer(playerId);
    }

    @Override
    public PlayerInfo getPlayerInfo() {
        return mplayerInfo;
    }

    private void getUserInfoFromServer(String playerId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getPlayerInfoRequest(playerId).enqueue(
                    new NostragamusCallBack<PlayerInfoResponse>() {
                        @Override
                        public void onResponse(Call<PlayerInfoResponse> call, Response<PlayerInfoResponse> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful()) {
                                mplayerInfo = response.body().getPlayerInfo();
                                mProfileModelListener.populatePlayerInfo();
                            }
                        }
                    }
            );
        }
    }

    public interface OnProfileModelListener {

        void onNoInternet();

        Context getContext();

        void populatePlayerInfo();
    }
}