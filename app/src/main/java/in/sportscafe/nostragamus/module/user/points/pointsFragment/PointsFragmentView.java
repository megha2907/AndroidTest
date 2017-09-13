package in.sportscafe.nostragamus.module.user.points.pointsFragment;

import android.os.Bundle;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by deepanshi on 9/13/17.
 */

public interface PointsFragmentView extends InAppView {

    void initMyPosition(ViewPagerAdapter adapter, int selectedPosition);

    void setMatchPoints(boolean isMatchPoints);

    void setUserLeaderBoardView(UserLeaderBoard userLeaderBoard);

    void setTabsView();

    void navigateToUserProfile(Bundle bundle);

    void navigateToHome();

}
