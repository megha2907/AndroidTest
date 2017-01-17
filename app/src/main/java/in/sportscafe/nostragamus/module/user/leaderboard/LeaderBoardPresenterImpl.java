package in.sportscafe.nostragamus.module.user.leaderboard;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;


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
        mLeaderBoardView.showInAppMessage("Your leaderboard will update here after a match you have played is over");
    }

    @Override
    public void setUserLeaderBoard(UserLeaderBoard userLeaderBoard) {
        mLeaderBoardView.setUserPoints(userLeaderBoard);
    }
}