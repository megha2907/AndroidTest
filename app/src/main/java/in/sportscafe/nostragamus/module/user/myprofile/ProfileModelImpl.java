package in.sportscafe.nostragamus.module.user.myprofile;

import android.content.Context;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingResponse;
import in.sportscafe.nostragamus.module.user.login.RefreshTokenModelImpl;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
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
public class ProfileModelImpl implements ProfileModel , UserInfoModelImpl.OnGetUserInfoModelListener {

    private OnProfileModelListener mProfileModelListener;

    private ProfileModelImpl(OnProfileModelListener listener) {
        this.mProfileModelListener = listener;
    }

    public static ProfileModel newInstance(OnProfileModelListener listener) {
        return new ProfileModelImpl(listener);
    }

    @Override
    public void getProfileDetails() {
        UserInfoModelImpl.newInstance(this).getUserInfo();
    }

    @Override
    public UserInfo getUserInfo() {
        return NostragamusDataHandler.getInstance().getUserInfo();
    }

    private void getLbSummary(final UserInfo updatedUserInfo) {
        MyWebService.getInstance().getLeaderBoardSummary().enqueue(
                new NostragamusCallBack<LbSummaryResponse>() {
                    @Override
                    public void onResponse(Call<LbSummaryResponse> call, Response<LbSummaryResponse> response) {
                        super.onResponse(call, response);
                        if(null == mProfileModelListener.getContext()) {
                            return;
                        }
                        if(response.isSuccessful()) {
                            mProfileModelListener.onGetProfileSuccess(updatedUserInfo, response.body().getLbSummary());
                        } else {
                            mProfileModelListener.onGetProfileFailed(response.message());
                        }
                    }
                }
        );

        MyWebService.getInstance().getLBLandingSummary(null, null, null, null)
                .enqueue(new NostragamusCallBack<LBLandingResponse>() {
                    @Override
                    public void onResponse(Call<LBLandingResponse> call, Response<LBLandingResponse> response) {
                        super.onResponse(call, response);

                    }
                });
    }

    @Override
    public void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo) {
        getLbSummary(updatedUserInfo);
    }

    @Override
    public void onFailedGetUpdateUserInfo(String message) {
        mProfileModelListener.onGetProfileFailed(message);
    }

    public interface OnProfileModelListener {

        void onGetProfileSuccess(UserInfo userInfo, LbSummary lbSummary);

        void onGetProfileFailed(String message);

        void onNoInternet();

        Context getContext();
    }
}