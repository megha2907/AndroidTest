package in.sportscafe.nostragamus.module.fuzzyplayers;

import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchFragment;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModel;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModelImpl;

/**
 * Created by deepanshu on 4/10/16.
 */

public class FuzzyPlayerFragment extends AbstractFuzzySearchFragment {

    public static FuzzyPlayerFragment newInstance() {
        return new FuzzyPlayerFragment();
    }

    @Override
    public AbstractFuzzySearchModel getModel(AbstractFuzzySearchModelImpl.OnFuzzySearchModelListener fuzzyPlayersPresenter) {
        return FuzzyPlayerModelImpl.newInstance(fuzzyPlayersPresenter);
    }
}