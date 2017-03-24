package in.sportscafe.nostragamus.module.question.tourlist;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */

public interface TourListView extends InAppView {

    void setAdapter(ViewPagerAdapter adapter);
}