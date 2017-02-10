package in.sportscafe.nostragamus.module.tournament;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */

public interface TournamentView extends InAppView {

    void setAdapter(ViewPagerAdapter adapter);
}