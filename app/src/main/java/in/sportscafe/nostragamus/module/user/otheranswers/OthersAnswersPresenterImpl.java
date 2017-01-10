package in.sportscafe.nostragamus.module.user.otheranswers;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by deepanshu on 12/10/16.
 */
public class OthersAnswersPresenterImpl implements OthersAnswersPresenter, OthersAnswersModelImpl.OnOthersAnswersModelListener {

    private OthersAnswersView mOthersAnswersView;

    private OthersAnswersModel mOthersAnswersModel;

    public OthersAnswersPresenterImpl(OthersAnswersView view) {
        this.mOthersAnswersView = view;
        this.mOthersAnswersModel = OthersAnswersModelImpl.newInstance(this);
    }

    public static OthersAnswersPresenter newInstance(OthersAnswersView view) {
        return new OthersAnswersPresenterImpl(view);
    }

    @Override
    public void onCreateOthersAnswers(Bundle bundle) {
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