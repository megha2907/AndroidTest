package in.sportscafe.nostragamus.module.fuzzysearch;

import android.os.Bundle;

/**
 * Created by deepanshu on 12/10/16.
 */
interface AbstractFuzzySearchPresenter {

    void onCreateFuzzyPlayers(Bundle bundle);

    void filterSearch(String key);
}