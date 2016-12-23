package in.sportscafe.nostragamus.module.play.myresults.flipPowerup;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by deepanshi on 12/20/16.
 */

public class FlipPresenterImpl implements FlipPresenter, FlipModelImpl.OnFlipModelListener {

    private FlipView mFlipView;

    private FlipModel mFlipModel;

    private FlipPresenterImpl(FlipView flipView) {
        this.mFlipView = flipView;
        this.mFlipModel = FlipModelImpl.newInstance(this);
    }

    public static FlipPresenterImpl newInstance(FlipView flipView) {
        return new FlipPresenterImpl(flipView);
    }

    @Override
    public void onCreateFlip(Bundle bundle) {
        mFlipModel.init(bundle);
        mFlipView.setAdapter(mFlipModel.getAdapter());
        getResultDetails();

    }

    private void getResultDetails() {
       // mFlipView.showProgressbar();
        mFlipModel.getMyResultsData(mFlipView.getContext());
    }


    @Override
    public void onSuccessMyResults(FlipAdapter FlipAdapter) {

    }

    @Override
    public void onFailedMyResults(String message) {
        mFlipView.dismissProgressbar();
        showAlertMessage(message);
    }

    @Override
    public void onNoInternet() {
        mFlipView.dismissProgressbar();
        showAlertMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    private void showAlertMessage(String message) {
        mFlipView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getResultDetails();
                    }
                });
    }

    @Override
    public void onEmpty() {
        mFlipView.dismissProgressbar();
        mFlipView.showInAppMessage(Constants.Alerts.NO_RESULTS_FOUND);
    }

    @Override
    public Context getContext() {
        return mFlipView.getContext();
    }


    @Override
    public void onFailedFlipPowerupResponse() {
        mFlipView.showMessage(Constants.Alerts.POWERUP_FAIL);
    }

    @Override
    public void onSuccessFlipPowerupResponse(Match match) {
        mFlipView.decreasePowerupCount();
    }


}
