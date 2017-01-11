package in.sportscafe.nostragamus.module.othersanswers;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by Jeeva on 15/6/16.
 */
public class OthersAnswersPresenterImpl implements OthersAnswersPresenter, OthersAnswersModelImpl.OnOthersAnswersModelListener {

    private OthersAnswersView mOthersAnswersView;

    private OthersAnswersModel mOthersAnswersModel;

    private OthersAnswersPresenterImpl(OthersAnswersView view) {
        this.mOthersAnswersView = view;
        this.mOthersAnswersModel = OthersAnswersModelImpl.newInstance(this);
    }

    public static OthersAnswersPresenterImpl newInstance(OthersAnswersView view) {
        return new OthersAnswersPresenterImpl(view);
    }

    @Override
    public void onCreateOthersAnswers(Bundle bundle) {
        Context context = mOthersAnswersView.getContext();
        if(null != context) {
            mOthersAnswersView.setAdapter(mOthersAnswersModel.getAdapter(context));
            mOthersAnswersModel.init(bundle);
            getOthersAnswers();
        }
    }

    private void getOthersAnswers() {
        mOthersAnswersView.showProgressbar();
        mOthersAnswersModel.getOthersAnswers();
    }

    @Override
    public void onSuccessOthersAnswers() {
        mOthersAnswersView.dismissProgressbar();
    }

    @Override
    public void onFailedOthersAnswers() {
        mOthersAnswersView.dismissProgressbar();
        showAlertMessage(Alerts.GETTING_OTHERS_ANSWERS_FAILED);
    }

    @Override
    public void onNoInternet() {
        mOthersAnswersView.dismissProgressbar();
        showAlertMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    private void showAlertMessage(String message) {
        mOthersAnswersView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getOthersAnswers();
                    }
                });
    }

    @Override
    public void onEmpty() {
        mOthersAnswersView.dismissProgressbar();
        mOthersAnswersView.showInAppMessage(Alerts.NO_RESULTS_FOUND);
    }

    @Override
    public boolean isThreadAlive() {
        return null != mOthersAnswersView.getContext();
    }
}