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
        mLeaderBoardView.setLeaderBoardAdapter(mLeaderBoardModel.getAdapter(mLeaderBoardView.getContext()));
        mLeaderBoardModel.init(bundle);
        mLeaderBoardView.moveAdapterPosition(mLeaderBoardModel.getUserPosition());
    }

    @Override
    public void update(Bundle bundle) {
        mLeaderBoardModel.refreshLeaderBoard(bundle);
    }

    @Override
    public void onEmpty() {
        mLeaderBoardView.showInAppMessage("Empty LeaderBoard");
    }
}