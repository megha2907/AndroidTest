package in.sportscafe.nostragamus.module.user.leaderboard;

import android.os.Bundle;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface LeaderBoardPresenter {

    void onCreateLeaderBoard(Bundle bundle);

    void checkSortType();

    void onClickUserPoints();
}