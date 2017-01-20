package in.sportscafe.nostragamus.module.fuzzysearch;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by deepanshu on 12/10/16.
 */
public class AbstractFuzzySearchPresenterImpl implements AbstractFuzzySearchPresenter,
        AbstractFuzzySearchModelImpl.OnFuzzySearchModelListener {

    private AbstractFuzzySearchView mOthersAnswersView;

    private AbstractFuzzySearchModel mOthersAnswersModel;

    public AbstractFuzzySearchPresenterImpl(AbstractFuzzySearchView view) {
        this.mOthersAnswersView = view;
        this.mOthersAnswersModel = mOthersAnswersView.getModel(this);
    }

    public static AbstractFuzzySearchPresenter newInstance(AbstractFuzzySearchView view) {
        return new AbstractFuzzySearchPresenterImpl(view);
    }

    @Override
    public void onCreateFuzzyPlayers(Bundle bundle) {
        Context context = mOthersAnswersView.getContext();
        if (null != context) {
            mOthersAnswersView.setAdapter(mOthersAnswersModel.createAdapter(context));
            mOthersAnswersModel.init(bundle);
        }
    }

    @Override
    public void filterSearch(String key) {
        mOthersAnswersModel.filterSearch(key);
    }

    @Override
    public void onFailed() {
        mOthersAnswersView.showMessage(Alerts.FUZZY_SEARCH_FAILED);
    }

    @Override
    public void onSearchItemSelected(String name) {
        mOthersAnswersView.setSearchItem(name);
    }

    @Override
    public void onNoInternet() {
        mOthersAnswersView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }
}