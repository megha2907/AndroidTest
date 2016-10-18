package in.sportscafe.scgame.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface LeaderBoardModel {

    void init(Bundle bundle);

    LeaderBoardAdapter getAdapter(Context context);

    void refreshLeaderBoard(Bundle bundle);
}