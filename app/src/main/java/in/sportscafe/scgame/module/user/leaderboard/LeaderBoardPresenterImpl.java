package in.sportscafe.scgame.module.user.leaderboard;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardPresenterImpl implements LeaderBoardPresenter {
    
    private LeaderBoardView mLeaderBoardView;
    
    private LeaderBoardModel mLeaderBoardModel;

    private LeaderBoardPresenterImpl(LeaderBoardView leaderBoardView) {
        this.mLeaderBoardView = leaderBoardView;
        this.mLeaderBoardModel = LeaderBoardModelImpl.newInstance();
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
    public void update(List<LeaderBoard> leaderBoardList) {
        mLeaderBoardModel.refreshLeaderBoard(leaderBoardList);
    }
}