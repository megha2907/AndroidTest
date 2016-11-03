package in.sportscafe.scgame.module.user.points;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.jeeva.android.InAppView;

import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.ViewPagerAdapter;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsView extends InAppView {

    void setName(String name);

    void initMyPosition(ViewPagerAdapter adapter, int selectedPosition);

    void setIcon(String icon);
}