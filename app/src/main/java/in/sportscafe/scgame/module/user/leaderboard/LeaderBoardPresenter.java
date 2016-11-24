package in.sportscafe.scgame.module.user.leaderboard;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface LeaderBoardPresenter {

    void onCreateLeaderBoard(Bundle bundle);

    void update(Bundle bundle);
}