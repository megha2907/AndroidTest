package in.sportscafe.nostragamus.module.user.points;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.popups.inapppopups.InAppPopupActivity;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class PointsPresenterImpl implements PointsPresenter, PointsModelImpl.OnPointsModelListener {

    private PointsView mPointsView;

    private PointsModel mPointsModel;
//
//    private OnLeaderBoardUpdateListener mLeaderBoardUpdateListener;


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
        if (!NostragamusDataHandler.getInstance().isVisitedLeaderBoards()) {
            mPointsView.showOtherProfilePopUp();
        }
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
    public void setChallengeTimer(String days, String hours, String mins, String secs) {
        mPointsView.setChallengeTimer(days, hours, mins, secs);
    }

    @Override
    public void setChallengeTimerView(boolean isChallengeTimer) {
        mPointsView.setChallengeTimerView(isChallengeTimer);
    }

    @Override
    public void setGroupHeadings(String groupName, String heading) {
        mPointsView.setGroupHeadings(groupName, heading);
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
        mPointsView.showInAppMessage("Your leaderboard will update here after a Match you have played is over");

    }



}