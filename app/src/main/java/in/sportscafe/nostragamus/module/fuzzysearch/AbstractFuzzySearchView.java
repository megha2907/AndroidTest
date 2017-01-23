package in.sportscafe.nostragamus.module.fuzzysearch;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshu on 12/10/16.
 */
interface AbstractFuzzySearchView extends InAppView {

    void setAdapter(Adapter adapter);

    AbstractFuzzySearchModel getModel(AbstractFuzzySearchModelImpl.OnFuzzySearchModelListener fuzzyPlayersPresenter);

    void setSearchItem(String name);
}