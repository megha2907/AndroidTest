package in.sportscafe.nostragamus.module.user.points;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsView extends InAppView {

    void setName(String name);

    void initMyPosition(ViewPagerAdapter adapter, int selectedPosition);

    void setIcon(String icon);

    void setMatchPoints(boolean isMatchPoints);

    void setChallengeTimerView(boolean isChallengeTimer);

    void setChallengeTimer(String days, String hours, String mins, String secs);

    void setGroupHeadings(String groupName,String heading);

    void setUserLeaderBoardView(UserLeaderBoard userLeaderBoard);

    void setTabsView();
}