package in.sportscafe.scgame.module.user.myprofile;

import android.content.Context;

import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.group.groupinfo.GrpListModelImpl;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
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
        getGrpList();
    }

    @Override
    public UserInfo getUserInfo() {
        return ScGameDataHandler.getInstance().getUserInfo();
    }

    private void getGrpList() {
        new GrpListModelImpl(new GrpListModelImpl.OnGrpListModelListener() {
            @Override
            public void onSuccessGrpList() {
                getLbSummary();
            }

            @Override
            public void onFailedGrpList(String message) {
                mProfileModelListener.onGetProfileFailed(message);
            }

            @Override
            public void onNoInternet() {
                mProfileModelListener.onNoInternet();
            }
        }).getGrpList();
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