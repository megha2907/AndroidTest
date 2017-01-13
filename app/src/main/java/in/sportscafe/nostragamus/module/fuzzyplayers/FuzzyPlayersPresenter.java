package in.sportscafe.nostragamus.module.fuzzyplayers;

import android.os.Bundle;

/**
 * Created by deepanshu on 12/10/16.
 */
interface FuzzyPlayersPresenter {

    void onCreateFuzzyPlayers(Bundle bundle);

    void filterSearch(String key);
}