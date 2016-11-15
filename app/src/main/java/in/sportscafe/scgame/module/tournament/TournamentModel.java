package in.sportscafe.scgame.module.tournament;

import android.os.Bundle;

import in.sportscafe.scgame.module.common.ViewPagerAdapter;

/**
 * Created by deepanshi on 11/14/16.
 */

public interface TournamentModel {

    ViewPagerAdapter getAdapter();

    void getTournaments();

    int getSelectedPosition();
}
