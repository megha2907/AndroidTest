package in.sportscafe.nostragamus.module.fuzzylbs;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchFragment;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModel;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModelImpl;

/**
 * Created by deepanshu on 4/10/16.
 */

public class FuzzyLbFragment extends AbstractFuzzySearchFragment {

    public static FuzzyLbFragment newInstance() {
        return new FuzzyLbFragment();
    }

    @Override
    public AbstractFuzzySearchModel getModel(AbstractFuzzySearchModelImpl.OnFuzzySearchModelListener fuzzyPlayersPresenter) {
        return FuzzyLbModelImpl.newInstance(fuzzyPlayersPresenter);
    }

    @Override
    public String getSearchHint() {
        return getResources().getString(R.string.fuzzy_lbs_hint);
    }
}