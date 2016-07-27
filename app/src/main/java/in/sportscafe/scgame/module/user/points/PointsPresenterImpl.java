package in.sportscafe.scgame.module.user.points;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsPresenterImpl implements PointsPresenter, PointsModelImpl.OnPointsModelListener {

    private PointsView mPointsView;

    private PointsModel mPointsModel;

    private OnLeaderBoardUpdateListener mLeaderBoardUpdateListener;

    private PointsPresenterImpl(PointsView pointsView) {
        this.mPointsView = pointsView;
        this.mPointsModel = PointsModelImpl.newInstance(this);
    }

    public static PointsPresenterImpl newInstance(PointsView pointsView) {
        return new PointsPresenterImpl(pointsView);
    }

    @Override
    public void onCreatePoints(OnLeaderBoardUpdateListener listener, Bundle bundle) {
        this.mLeaderBoardUpdateListener = listener;

        mPointsModel.init(bundle);

        mPointsView.setGroupAdapter(mPointsModel.getGroupAdapter(mPointsView.getContext()),
                mPointsModel.getInitialGroupPosition());
        mPointsView.setSportAdapter(mPointsModel.getSportsAdapter(mPointsView.getContext()),
                mPointsModel.getInitialSportPosition());

        onAllTimeClicked();

        mPointsModel.setInitialSetDone();
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
    public void onWeekClicked() {
        mPointsModel.onWeekSelected();
    }

    @Override
    public void onMonthClicked() {
        mPointsModel.onMonthSelected();
    }

    @Override
    public void onAllTimeClicked() {
        mPointsModel.onAllTimeSelected();
    }

    @Override
    public void onGettingLeaderBoard() {
        mPointsView.showProgressbar();
    }

    @Override
    public void onEmpty() {
        mPointsView.dismissProgressbar();
        mPointsView.showInAppMessage(Constants.Alerts.NO_LEADERBOARD);
    }

    @Override
    public void onFailureLeaderBoard(String message) {
        mPointsView.dismissProgressbar();
        mPointsView.showInAppMessage(message);
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
    public void onGetLeaderBoardData(List<LeaderBoard> leaderBoardList) {
        mPointsView.dismissProgressbar();
        mLeaderBoardUpdateListener.updateLeaderBoard(leaderBoardList);
    }
}