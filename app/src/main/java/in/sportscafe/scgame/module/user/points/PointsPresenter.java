package in.sportscafe.scgame.module.user.points;

import android.os.Bundle;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsPresenter {

    void onCreatePoints(OnLeaderBoardUpdateListener listener, Bundle bundle);

    void onGroupItemSelected(int position);

    void onSportItemSelected(int position);

    void onWeekClicked();

    void onMonthClicked();

    void onAllTimeClicked();
}