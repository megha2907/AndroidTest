package in.sportscafe.nostragamus.module.user.points.pointsFragment;

import android.os.Bundle;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.points.PointsModel;
import in.sportscafe.nostragamus.module.user.points.PointsPresenter;

/**
 * Created by deepanshi on 9/13/17.
 */

public class PointsFragmentPresenterImpl implements PointsPresenter, PointsFragmentModeImpl.OnPointsModelListener {

    private PointsFragmentView mPointsView;

    private PointsModel mPointsModel;


    private PointsFragmentPresenterImpl(PointsFragmentView pointsView) {
        this.mPointsView = pointsView;
        this.mPointsModel = PointsFragmentModeImpl.newInstance(this, mPointsView.getActivity().getSupportFragmentManager());
    }

    public static PointsFragmentPresenterImpl newInstance(PointsFragmentView pointsView) {
        return new PointsFragmentPresenterImpl(pointsView);
    }

    @Override
    public void onCreatePoints(Bundle bundle) {
        mPointsModel.init(bundle);
        refreshLb();
    }

    @Override
    public void onSortByPoints() {
        Log.i("sort", "onSortByPoints");
        mPointsModel.sortAdapter("rank");
    }

    @Override
    public void updateUserLeaderBoard(int position) {
        mPointsModel.updateUserLeaderBoard(position);
    }

    @Override
    public void onClickUserPoints() {
        mPointsView.navigateToUserProfile(mPointsModel.getUserProfileBundle());
    }

    @Override
    public void onNoInternet() {
        showAlertMsg(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void setIsMatchPoints(boolean isMatchPoints) {
        mPointsView.setMatchPoints(isMatchPoints);
    }


    @Override
    public void setUserLeaderBoard(UserLeaderBoard userLeaderBoard) {
        mPointsView.setUserLeaderBoardView(userLeaderBoard);
    }

    @Override
    public void changeTabsView() {
        mPointsView.setTabsView();
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
//        mPointsView.showProgressbar();
        mPointsModel.refreshLeaderBoard();
    }

    @Override
    public void onSuccessLeaderBoard() {
       // mPointsView.dismissProgressbar();
        mPointsView.initMyPosition(mPointsModel.getAdapter(), mPointsModel.getSelectedPosition());
    }

    @Override
    public void onEmpty() {
      //  mPointsView.dismissProgressbar();
        //mPointsView.showInAppMessage("Your leaderboard will update here after a Match you have played is over");

    }

}