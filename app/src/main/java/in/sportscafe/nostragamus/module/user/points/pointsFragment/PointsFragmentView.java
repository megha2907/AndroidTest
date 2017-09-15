package in.sportscafe.nostragamus.module.user.points.pointsFragment;

import android.os.Bundle;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.points.pointsFragment.dto.UserLeaderBoardInfo;

/**
 * Created by deepanshi on 9/13/17.
 */

public interface PointsFragmentView extends InAppView {

    void updateUserLeaderBoard(int selectedPosition);

    void setMatchPoints(boolean isMatchPoints);

    void setUserLeaderBoardView(UserLeaderBoard userLeaderBoard);

    void navigateToUserProfile(Bundle bundle);

    void navigateToHome();

    void onSuccessLeaderBoardInfo(UserLeaderBoardInfo userLeaderBoardInfo);
}
