package in.sportscafe.scgame.module.user.points;

import android.widget.ArrayAdapter;

import com.jeeva.android.InAppView;

import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsView extends InAppView {

    void setGroupAdapter(ArrayAdapter<GroupInfo> groupAdapter, int initialGroupPosition);

    void setSportAdapter(ArrayAdapter<Sport> sportAdapter, int initialSportPosition);
}