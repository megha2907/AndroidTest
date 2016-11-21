package in.sportscafe.scgame.module.user.points;

import com.jeeva.android.InAppView;

import in.sportscafe.scgame.module.common.ViewPagerAdapter;

/**
 * Created by Jeeva on 10/6/16.
 */
public interface PointsView extends InAppView {

    void setName(String name);

    void initMyPosition(ViewPagerAdapter adapter, int selectedPosition);

    void setIcon(String icon);
}