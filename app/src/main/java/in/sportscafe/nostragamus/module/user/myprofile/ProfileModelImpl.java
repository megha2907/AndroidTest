package in.sportscafe.nostragamus.module.user.myprofile;

import android.content.Context;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummaryResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ProfileModelImpl implements ProfileModel {

    private OnProfileModelListener mProfileModelListener;

    private ProfileModelImpl(OnProfileModelListener listener) {
        this.mProfileModelListener = listener;
    }

    public static ProfileModel newInstance(OnProfileModelListener listener) {
        return new ProfileModelImpl(listener);
    }

    @Override
    public void getProfileDetails() {
        getLbSummary();
        getUserInfoFromServer();
    }

    @Override
    public UserInfo getUserInfo() {
        return NostragamusDataHandler.getInstance().getUserInfo();
    }

    private void getUserInfoFromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserInfoRequest(NostragamusDataHandler.getInstance().getUserId()).enqueue(
                    new NostragamusCallBack<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful()) {
                                UserInfo updatedUserInfo = response.body().getUserInfo();

                                if (null != updatedUserInfo) {
                                    NostragamusDataHandler.getInstance().setUserInfo(updatedUserInfo);
                                    NostragamusDataHandler.getInstance().setNumberof2xPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                    NostragamusDataHandler.getInstance().setNumberofNonegsPowerups(updatedUserInfo.getPowerUps().get("no_negs"));
                                    NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(updatedUserInfo.getPowerUps().get("player_poll"));
                                   // NostragamusDataHandler.getInstance().setNumberofReplayPowerups(updatedUserInfo.getPowerUps().get("match_replay"));
                                    NostragamusDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());
                                    NostragamusDataHandler.getInstance().setNumberofGroups(updatedUserInfo.getNumberofgroups());
                                }
                            }
                        }
                    }
            );
        }
    }

    private void getLbSummary() {
        MyWebService.getInstance().getLeaderBoardSummary().enqueue(
                new NostragamusCallBack<LbSummaryResponse>() {
                    @Override
                    public void onResponse(Call<LbSummaryResponse> call, Response<LbSummaryResponse> response) {
                        super.onResponse(call, response);
                        if(null == mProfileModelListener.getContext()) {
                            return;
                        }
                        if(response.isSuccessful()) {
                            mProfileModelListener.onGetProfileSuccess(response.body().getLbSummary());
                        } else {
                            mProfileModelListener.onGetProfileFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface OnProfileModelListener {

        void onGetProfileSuccess(LbSummary lbSummary);

        void onGetProfileFailed(String message);

        void onNoInternet();

        Context getContext();
    }
}