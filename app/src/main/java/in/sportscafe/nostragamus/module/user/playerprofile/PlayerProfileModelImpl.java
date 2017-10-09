package in.sportscafe.nostragamus.module.user.playerprofile;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
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

    private Integer mRoomId;

    private PlayerInfo mPlayerInfo;

    private PlayerProfileModelImpl.OnProfileModelListener mProfileModelListener;

    private PlayerProfileModelImpl(PlayerProfileModelImpl.OnProfileModelListener listener) {
        this.mProfileModelListener = listener;
    }

    public static PlayerProfileModel newInstance(PlayerProfileModelImpl.OnProfileModelListener listener) {
        return new PlayerProfileModelImpl(listener);
    }

    @Override
    public void getProfileDetails(Bundle bundle) {
        if(bundle.containsKey(BundleKeys.ROOM_ID)) {
            mRoomId = bundle.getInt(BundleKeys.ROOM_ID);
        }

        getPlayerInfoFromServer(bundle.getInt(BundleKeys.PLAYER_ID));
    }

    @Override
    public PlayerInfo getPlayerInfo() {
        return mPlayerInfo;
    }

    @Override
    public void getPlayerInfoFromServer(Integer playerId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getPlayerInfoRequest(playerId).enqueue(
                    new NostragamusCallBack<PlayerInfoResponse>() {
                        @Override
                        public void onResponse(Call<PlayerInfoResponse> call, Response<PlayerInfoResponse> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful()) {
                                mPlayerInfo = response.body().getPlayerInfo();
                                mProfileModelListener.onSuccessPlayerInfo(mPlayerInfo);
                            } else {
                                mProfileModelListener.onFailedPlayerInfo();
                            }
                        }
                    }
            );
        }
    }

    @Override
    public Integer getRoomId() {
        return mRoomId;
    }

    public interface OnProfileModelListener {

        void onNoInternet();

        void onSuccessPlayerInfo(PlayerInfo playerInfo);

        void onFailedPlayerInfo();
    }
}