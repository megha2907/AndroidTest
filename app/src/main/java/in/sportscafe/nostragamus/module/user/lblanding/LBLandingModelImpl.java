package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.Context;

import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 11/7/16.
 */

public class LBLandingModelImpl implements LBLandingModel {

    private OnLBLandingModelListener mLbLandingModelListener;

    private LBLandingModelImpl(LBLandingModelImpl.OnLBLandingModelListener listener) {
        mLbLandingModelListener =listener;
    }

    public static LBLandingModel newInstance(LBLandingModelImpl.OnLBLandingModelListener listener) {
        return new LBLandingModelImpl(listener);
    }

    @Override
    public void getLeaderBoardSummary(){
        getLbSummary();
    }

    private void getLbSummary() {
        MyWebService.getInstance().getLBLandingSummary().enqueue(
                new NostragamusCallBack<LBLandingResponse>() {
                    @Override
                    public void onResponse(Call<LBLandingResponse> call, Response<LBLandingResponse> response) {
                        super.onResponse(call, response);
                        if(null == mLbLandingModelListener.getContext()) {
                            return;
                        }
                        if(response.isSuccessful()) {
                            mLbLandingModelListener.onGetLBLandingSuccess(response.body().getSummary());
                        } else {
                            mLbLandingModelListener.onGetLBLandingFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface OnLBLandingModelListener {

        void onGetLBLandingSuccess(LBLandingSummary lbSummary);

        void onGetLBLandingFailed(String message);

        void onNoInternet();

        Context getContext();
    }
}