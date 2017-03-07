package in.sportscafe.nostragamus.module.user.leaderboard;

import android.os.Bundle;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.sportselection.profilesportselection.ProfileSportSelectionFragment;


/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardPresenterImpl implements LeaderBoardPresenter, LeaderBoardModelImpl.OnLeaderBoardModelListener {
    
    private LeaderBoardView mLeaderBoardView;
    
    private LeaderBoardModel mLeaderBoardModel;

    private LeaderBoardFragment.OnGetLeaderBoardListener mGetLeaderBoardListener;

    private LeaderBoardPresenterImpl(LeaderBoardView leaderBoardView,LeaderBoardFragment.OnGetLeaderBoardListener getLeaderBoardListener) {
        this.mLeaderBoardView = leaderBoardView;
        this.mLeaderBoardModel = LeaderBoardModelImpl.newInstance(this);
        mGetLeaderBoardListener = getLeaderBoardListener;
    }

    public static LeaderBoardPresenter newInstance(LeaderBoardView leaderBoardView, LeaderBoardFragment.OnGetLeaderBoardListener getLeaderBoardListener) {
        return new LeaderBoardPresenterImpl(leaderBoardView,getLeaderBoardListener);
    }

    @Override
    public void onCreateLeaderBoard(Bundle bundle) {
        mLeaderBoardView.setLeaderBoardAdapter(mLeaderBoardModel.getAdapter(mLeaderBoardView.getContext()));
        mLeaderBoardModel.init(bundle);
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
        mLeaderBoardView.showInAppMessage("Your leaderboard will update here after a match you have played is over");
    }

    @Override
    public void setUserLeaderBoard(UserLeaderBoard userLeaderBoard) {
       // mLeaderBoardView.setUserPoints(userLeaderBoard);
        mGetLeaderBoardListener.onGetUserLeaderBoard(userLeaderBoard);
    }
}