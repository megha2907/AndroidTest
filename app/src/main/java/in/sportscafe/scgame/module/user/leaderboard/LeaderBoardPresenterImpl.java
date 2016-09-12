package in.sportscafe.scgame.module.user.leaderboard;

import android.os.Bundle;
import android.view.View;

import in.sportscafe.scgame.Constants;


/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardPresenterImpl implements LeaderBoardPresenter, LeaderBoardModelImpl.OnLeaderBoardModelListener {
    
    private LeaderBoardView mLeaderBoardView;
    
    private LeaderBoardModel mLeaderBoardModel;

    private LeaderBoardPresenterImpl(LeaderBoardView leaderBoardView) {
        this.mLeaderBoardView = leaderBoardView;
        this.mLeaderBoardModel = LeaderBoardModelImpl.newInstance(this);
    }

    public static LeaderBoardPresenter newInstance(LeaderBoardView leaderBoardView) {
        return new LeaderBoardPresenterImpl(leaderBoardView);
    }

    @Override
    public void onCreateLeaderBoard(Bundle bundle) {
        mLeaderBoardModel.init(bundle);
        mLeaderBoardView.setLeaderBoardAdapter(mLeaderBoardModel
                .getAdapter(mLeaderBoardView.getContext()));
    }

    @Override
    public void update(Bundle bundle) {
        mLeaderBoardView.showProgressbar();
        mLeaderBoardModel.refreshLeaderBoard(bundle);
    }



    @Override
    public void onFailureLeaderBoard(String message) {
        mLeaderBoardView.dismissProgressbar();
        mLeaderBoardView.showMessage(message, "RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onSuccessLeaderBoard() {
        mLeaderBoardView.dismissProgressbar();

    }

    @Override
    public void onEmpty() {
        mLeaderBoardView.dismissProgressbar();
        mLeaderBoardView.showInAppMessage("Empty LeaderBoard");

    }

    @Override
    public void onNoInternet() {
        mLeaderBoardView.dismissProgressbar();
        mLeaderBoardView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION, "RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}