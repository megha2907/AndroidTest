package in.sportscafe.scgame.module.tournament;

import com.jeeva.android.InAppView;

import in.sportscafe.scgame.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */

public interface TournamentView extends InAppView {
    void initMyPosition(ViewPagerAdapter adapter, int selectedPosition);

    void setAdapter(ViewPagerAdapter adapter);
}
