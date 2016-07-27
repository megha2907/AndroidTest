package in.sportscafe.scgame.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardModelImpl implements LeaderBoardModel {

    private LeaderBoardAdapter mLeaderBoardAdapter;

    private LeaderBoardModelImpl() {
    }

    public static LeaderBoardModel newInstance() {
        return new LeaderBoardModelImpl();
    }

    @Override
    public void init(Bundle bundle) {

    }

    @Override
    public LeaderBoardAdapter getAdapter(Context context) {
        mLeaderBoardAdapter = new LeaderBoardAdapter(context);
        return mLeaderBoardAdapter;
    }

    @Override
    public void refreshLeaderBoard(List<LeaderBoard> leaderBoardList) {
        mLeaderBoardAdapter.clear();
        mLeaderBoardAdapter.addAll(leaderBoardList);
        mLeaderBoardAdapter.notifyDataSetChanged();
    }
}