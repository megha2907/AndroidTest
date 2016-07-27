package in.sportscafe.scgame.module.user.points;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsModel {

    void init(Bundle bundle);

    ArrayAdapter<GroupInfo> getGroupAdapter(Context context);

    ArrayAdapter<Sport> getSportsAdapter(Context context);

    int getInitialGroupPosition();

    int getInitialSportPosition();

    void setInitialSetDone();

    void onGroupSelected(int position);

    void onSportSelected(int position);

    void onWeekSelected();

    void onMonthSelected();

    void onAllTimeSelected();

    void refreshLeaderBoard();
}