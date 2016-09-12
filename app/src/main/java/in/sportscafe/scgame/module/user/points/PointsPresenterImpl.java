package in.sportscafe.scgame.module.user.points;

import android.os.Bundle;
import android.view.View;

import in.sportscafe.scgame.Constants;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsPresenterImpl implements PointsPresenter, PointsModelImpl.OnPointsModelListener {

    private PointsView mPointsView;

    private PointsModel mPointsModel;



    private PointsPresenterImpl(PointsView pointsView) {
        this.mPointsView = pointsView;
        this.mPointsModel = PointsModelImpl.newInstance(this);
    }

    public static PointsPresenterImpl newInstance(PointsView pointsView) {
        return new PointsPresenterImpl(pointsView);
    }

    @Override
    public void onCreatePoints(Bundle bundle) {


        mPointsModel.init(bundle);

        mPointsView.setGroupAdapter(mPointsModel.getGroupAdapter(mPointsView.getContext()),
                mPointsModel.getInitialGroupPosition());
        mPointsView.setSportAdapter(mPointsModel.getSportsAdapter(mPointsView.getContext()),
                mPointsModel.getInitialSportPosition());

        mPointsModel.setInitialSetDone();

        mPointsModel.refreshLeaderBoard();
    }

    @Override
    public void onGroupItemSelected(int position) {
        mPointsModel.onGroupSelected(position);
    }

    @Override
    public void onSportItemSelected(int position) {
        mPointsModel.onSportSelected(position);
    }


    @Override
    public void onNoInternet() {
        mPointsView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION,
                "RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPointsModel.refreshLeaderBoard();
                    }
                });
    }

    @Override
    public void onSelectionChanged(Bundle bundle) {
        mPointsView.refreshLeaderBoard(bundle);

    }


}