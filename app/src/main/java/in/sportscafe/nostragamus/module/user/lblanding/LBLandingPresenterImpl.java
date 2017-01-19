package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.Context;
import android.view.View;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by deepanshi on 11/7/16.
 */

public class LBLandingPresenterImpl implements LBLandingPresenter, LBLandingModelImpl.OnLBLandingModelListener {

    private LBLandingView mLbLandingView;

    private LBLandingModel mLbLandingModel;


    private LBLandingPresenterImpl(LBLandingView leaderBoardSummaryView) {
        this.mLbLandingView = leaderBoardSummaryView;
        this.mLbLandingModel = LBLandingModelImpl.newInstance(this);
    }

    public static LBLandingPresenter newInstance(LBLandingView leaderBoardSummaryView) {
        return new LBLandingPresenterImpl(leaderBoardSummaryView);
    }

    @Override
    public void onCreateLeaderBoard() {
        getLeaderBoardSummary();
    }

    private void getLeaderBoardSummary() {
        mLbLandingView.showProgressbar();
        mLbLandingModel.getLeaderBoardSummary();
    }

    @Override
    public void onGetLBLandingSuccess(LBLandingSummary lbSummary) {
        mLbLandingView.dismissProgressbar();
        mLbLandingView.initMyPosition(lbSummary);
    }

    @Override
    public void onGetLBLandingFailed(String message) {
        mLbLandingView.dismissProgressbar();
        showAlert(message);
    }

    @Override
    public void onNoInternet() {
        mLbLandingView.dismissProgressbar();
        showAlert(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public Context getContext() {
        return mLbLandingView.getContext();
    }

    private void showAlert(String message) {
        mLbLandingView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLeaderBoardSummary();
                    }
                });
    }
}
