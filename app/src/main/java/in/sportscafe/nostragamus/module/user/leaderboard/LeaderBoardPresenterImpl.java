package in.sportscafe.nostragamus.module.user.leaderboard;

import android.os.Bundle;


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
        mLeaderBoardView.setLeaderBoardAdapter(mLeaderBoardModel.getAdapter(mLeaderBoardView.getContext()));
        checkSortType();
    }

    @Override
    public void update(Bundle bundle) {
        checkSortType();
    }

    @Override
    public void checkSortType() {
        mLeaderBoardModel.sortAndRefreshLeaderBoard();
        //mLeaderBoardView.moveAdapterPosition(mLeaderBoardModel.getUserPosition());
        //mLeaderBoardModel.setSortType(0);
    }

    @Override
    public void onEmpty() {
        mLeaderBoardView.showInAppMessage("Your leaderboard will update here after a Match you have played is over");
    }

}