package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 11/7/16.
 */

public class LBLandingModelImpl implements LBLandingModel {

    private Integer mSportsId;

    private Integer mGroupId;

    private Integer mChallengeId;

    private Integer mTourId;

    private OnLBLandingModelListener mLbLandingModelListener;

    private boolean mEmptyDone = false;

    private LBLandingModelImpl(LBLandingModelImpl.OnLBLandingModelListener listener) {
        mLbLandingModelListener = listener;
    }

    public static LBLandingModel newInstance(LBLandingModelImpl.OnLBLandingModelListener listener) {
        return new LBLandingModelImpl(listener);
    }

    private void resetAllValues() {
        mSportsId = null;
        mGroupId = null;
        mChallengeId = null;
        mTourId = null;
    }

    @Override
    public void getLeaderBoardSummary(Bundle bundle) {
        resetAllValues();

        if (null != bundle && bundle.containsKey(BundleKeys.LB_LANDING_DATA)) {
            LbLanding lbLanding = (LbLanding) bundle.getSerializable(BundleKeys.LB_LANDING_DATA);
            switch (lbLanding.getType()) {
                case LBLandingType.SPORT:
                    mSportsId = lbLanding.getId();
                    break;
                case LBLandingType.GROUP:
                    mGroupId = lbLanding.getId();
                    break;
                case LBLandingType.CHALLENGE:
                    mChallengeId = lbLanding.getId();
                    break;
                case LBLandingType.TOURNAMENT:
                    mTourId = lbLanding.getId();
                    break;
            }
            mEmptyDone = false;
            getLbSummary();
        } else if(!mEmptyDone) {
            mEmptyDone = true;
            getLbSummary();
        }
    }

    private void getLbSummary() {
        MyWebService.getInstance().getLBLandingSummary(mSportsId, mGroupId, mChallengeId, mTourId).enqueue(
                new NostragamusCallBack<LBLandingResponse>() {
                    @Override
                    public void onResponse(Call<LBLandingResponse> call, Response<LBLandingResponse> response) {
                        super.onResponse(call, response);
                        if (null == mLbLandingModelListener.getContext()) {
                            return;
                        }
                        if (response.isSuccessful()) {
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