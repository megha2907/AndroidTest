package in.sportscafe.nostragamus.module.user.leaderboard;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface LeaderBoardModel {

    void init(Bundle bundle);

    LeaderBoardAdapter getAdapter(Context context);

    int getUserPosition();

    void refreshLeaderBoard(Bundle bundle);
}