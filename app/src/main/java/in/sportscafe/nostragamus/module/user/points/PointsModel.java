package in.sportscafe.nostragamus.module.user.points;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

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

}