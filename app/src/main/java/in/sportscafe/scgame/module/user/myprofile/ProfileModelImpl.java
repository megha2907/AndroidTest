package in.sportscafe.scgame.module.user.myprofile;

import android.content.Context;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.group.groupinfo.GrpListModelImpl;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummaryResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
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
        return ScGameDataHandler.getInstance().getUserInfo();
    }

    private void getUserInfoFromServer() {
        if (ScGame.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserInfoRequest(ScGameDataHandler.getInstance().getUserId()).enqueue(
                    new ScGameCallBack<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            if (response.isSuccessful()) {
                                UserInfo updatedUserInfo = response.body().getUserInfo();

                                if (null != updatedUserInfo) {
                                    ScGameDataHandler.getInstance().setUserInfo(updatedUserInfo);
                                    ScGameDataHandler.getInstance().setNumberofPowerups(updatedUserInfo.getPowerUps().get("2x"));
                                    ScGameDataHandler.getInstance().setNumberofBadges(updatedUserInfo.getBadges().size());
                                    ScGameDataHandler.getInstance().setNumberofGroups(updatedUserInfo.getNumberofgroups());
                                }
                            }
                        }
                    }
            );
        }
    }

    private void getLbSummary() {
        MyWebService.getInstance().getLeaderBoardSummary().enqueue(
                new ScGameCallBack<LbSummaryResponse>() {
                    @Override
                    public void onResponse(Call<LbSummaryResponse> call, Response<LbSummaryResponse> response) {
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