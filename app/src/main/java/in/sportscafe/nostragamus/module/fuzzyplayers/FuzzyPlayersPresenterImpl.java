package in.sportscafe.nostragamus.module.fuzzyplayers;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by deepanshu on 12/10/16.
 */
public class FuzzyPlayersPresenterImpl implements FuzzyPlayersPresenter, FuzzyPlayersModelImpl.OnOthersAnswersModelListener {

    private FuzzyPlayersView mOthersAnswersView;

    private FuzzyPlayersModel mOthersAnswersModel;

    public FuzzyPlayersPresenterImpl(FuzzyPlayersView view) {
        this.mOthersAnswersView = view;
        this.mOthersAnswersModel = FuzzyPlayersModelImpl.newInstance(this);
    }

    public static FuzzyPlayersPresenter newInstance(FuzzyPlayersView view) {
        return new FuzzyPlayersPresenterImpl(view);
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
    public void onNoInternet() {
        mOthersAnswersView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }
}