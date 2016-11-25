package in.sportscafe.nostragamus.module.user.points;

import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsPresenterImpl implements PointsPresenter, PointsModelImpl.OnPointsModelListener {

    private PointsView mPointsView;

    private PointsModel mPointsModel;


    private PointsPresenterImpl(PointsView pointsView) {
        this.mPointsView = pointsView;
        this.mPointsModel = PointsModelImpl.newInstance(this, mPointsView.getActivity().getSupportFragmentManager());
    }

    public static PointsPresenterImpl newInstance(PointsView pointsView) {
        return new PointsPresenterImpl(pointsView);
    }

    @Override
    public void onCreatePoints(Bundle bundle) {
        mPointsModel.init(bundle);
        mPointsView.setName(mPointsModel.getName());
        mPointsView.setIcon(mPointsModel.getIcon());
        refreshLb();
    }

    @Override
    public void onNoInternet() {
        showAlertMsg(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailureLeaderBoard(String message) {
        showAlertMsg(message);
    }

    private void showAlertMsg(String message) {
        mPointsView.dismissProgressbar();
        mPointsView.showMessage(message, "RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLb();
            }
        });
    }

    private void refreshLb() {
        mPointsView.showProgressbar();
        mPointsModel.refreshLeaderBoard();
    }

    @Override
    public void onSuccessLeaderBoard() {
        mPointsView.dismissProgressbar();
        mPointsView.initMyPosition(mPointsModel.getAdapter(), mPointsModel.getSelectedPosition());
    }

    @Override
    public void onEmpty() {
        mPointsView.dismissProgressbar();
        mPointsView.showInAppMessage("Your leaderboard will update here after a match you have played is over");

    }

}