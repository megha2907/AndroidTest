package in.sportscafe.scgame.module.user.leaderboardsummary;

import android.content.Context;
import android.view.View;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.module.user.myprofile.ProfileModel;
import in.sportscafe.scgame.module.user.myprofile.ProfileModelImpl;
import in.sportscafe.scgame.module.user.myprofile.ProfilePresenter;
import in.sportscafe.scgame.module.user.myprofile.ProfilePresenterImpl;
import in.sportscafe.scgame.module.user.myprofile.ProfileView;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by deepanshi on 11/7/16.
 */

public class LeaderBoardSummaryPresenterImpl implements LeaderBoardSummaryPresenter, LeaderBoardSummaryModelImpl.OnLeaderBoardSummaryModelListener {

    private LeaderBoardSummaryView mleaderBoardSummaryView;

    private LeaderBoardSummaryModel mleaderBoardSummaryModel;


    private LeaderBoardSummaryPresenterImpl(LeaderBoardSummaryView leaderBoardSummaryView) {
        this.mleaderBoardSummaryView = leaderBoardSummaryView;
        this.mleaderBoardSummaryModel = LeaderBoardSummaryModelImpl.newInstance(this);
    }

    public static LeaderBoardSummaryPresenter newInstance(LeaderBoardSummaryView leaderBoardSummaryView) {
        return new LeaderBoardSummaryPresenterImpl(leaderBoardSummaryView);
    }

    @Override
    public void onCreateLeaderBoard() {
        getLeaderBoardSummary();
    }

    private void getLeaderBoardSummary() {
        mleaderBoardSummaryView.showProgressbar();
        mleaderBoardSummaryModel.getLeaderBoardSummary();
    }


    @Override
    public void onGetLeaderBoardSummarySuccess(LbSummary lbSummary) {
        mleaderBoardSummaryView.dismissProgressbar();
        mleaderBoardSummaryView.initMyPosition(lbSummary);
    }

    @Override
    public void onGetLeaderBoardSummaryFailed(String message) {
        mleaderBoardSummaryView.dismissProgressbar();
        showAlert(message);
    }

    @Override
    public void onNoInternet() {
        mleaderBoardSummaryView.dismissProgressbar();
        showAlert(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public Context getContext() {
        return mleaderBoardSummaryView.getContext();
    }

    private void showAlert(String message) {
        mleaderBoardSummaryView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLeaderBoardSummary();
                    }
                });
    }
}
