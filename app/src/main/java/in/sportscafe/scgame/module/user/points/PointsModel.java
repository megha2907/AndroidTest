package in.sportscafe.scgame.module.user.points;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsModel {

    void init(Bundle bundle);

    ArrayAdapter<GroupInfo> getGroupAdapter(Context context);

    ArrayAdapter<TournamentInfo> getSportsAdapter(Context context);

    int getInitialGroupPosition();

    int getInitialSportPosition();

    void setInitialSetDone();

    void onGroupSelected(int position);

    void onSportSelected(int position);

    void refreshLeaderBoard();
}