package in.sportscafe.nostragamus.module.fuzzyplayers;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchFragment;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModel;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModelImpl;
import in.sportscafe.nostragamus.module.user.group.members.MembersFragment;

/**
 * Created by deepanshu on 4/10/16.
 */

public class FuzzyPlayerFragment extends AbstractFuzzySearchFragment {


    public static FuzzyPlayerFragment newInstance(Integer matchId) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.MATCH_ID, matchId);

        FuzzyPlayerFragment fragment = new FuzzyPlayerFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public AbstractFuzzySearchModel getModel(AbstractFuzzySearchModelImpl.OnFuzzySearchModelListener fuzzyPlayersPresenter) {
        return FuzzyPlayerModelImpl.newInstance(fuzzyPlayersPresenter,getArguments().getInt(Constants.BundleKeys.MATCH_ID));
    }

    @Override
    public String getSearchHint() {
        return getResources().getString(R.string.fuzzy_players_hint);
    }
}