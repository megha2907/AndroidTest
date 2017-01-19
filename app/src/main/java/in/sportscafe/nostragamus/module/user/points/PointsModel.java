package in.sportscafe.nostragamus.module.user.points;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsModel {

    void init(Bundle bundle);

    String getName();

    String getIcon();

    ViewPagerAdapter getAdapter();

    void refreshLeaderBoard();

    int getSelectedPosition();

    List<LeaderBoard> getLeaderBoardList();

    void refreshAdapter(List<LeaderBoard> leaderBoardList, String SortType);

    void sortAdapter(String rank);
}