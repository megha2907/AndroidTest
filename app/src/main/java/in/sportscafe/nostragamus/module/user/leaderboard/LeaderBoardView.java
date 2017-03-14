package in.sportscafe.nostragamus.module.user.leaderboard;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface LeaderBoardView extends InAppView {

    void setLeaderBoardAdapter(LeaderBoardAdapter leaderBoardAdapter);

    void moveAdapterPosition(int movePosition);

}