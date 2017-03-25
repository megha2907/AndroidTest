package in.sportscafe.nostragamus.module.question.add;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by Jeeva on 24/03/17.
 */
public class AddQuestionPresenterImpl implements AddQuestionPresenter, AddQuestionModelImpl.OnAddQuestionModelListener {

    private AddQuestionView mAddQuestionView;

    private AddQuestionModel mAddQuestionModel;

    private AddQuestionPresenterImpl(AddQuestionView view) {
        this.mAddQuestionView = view;
        this.mAddQuestionModel = AddQuestionModelImpl.newInstance(this);
    }

    public static AddQuestionPresenter newInstance(AddQuestionView view) {
        return new AddQuestionPresenterImpl(view);
    }

    @Override
    public void onCreateAddQuestion(Bundle bundle) {
        mAddQuestionModel.init(bundle);
    }

    @Override
    public void onClickAdd(String question, String context, String leftOption, String rightOption, String neitherOption) {
        mAddQuestionModel.saveQuestion(question, context, leftOption, rightOption, neitherOption);
    }

    @Override
    public void onApiCallStarted() {
        mAddQuestionView.showProgressbar();
    }

    @Override
    public boolean onApiCallStopped() {
        return mAddQuestionView.dismissProgressbar();
    }

    @Override
    public void onNeedMoreCharForQuestion() {
        mAddQuestionView.showMessage(Alerts.QUESTION_LIMIT_ALERT);
    }

    @Override
    public void onNeedMoreCharForContext() {
        mAddQuestionView.showMessage(Alerts.CONTEXT_LIMIT_ALERT);
    }

    @Override
    public void onEmptyLeftOption() {
        mAddQuestionView.showMessage(Alerts.LEFT_OPTION_EMPTY);
    }

    @Override
    public void onEmptyRightOption() {
        mAddQuestionView.showMessage(Alerts.RIGHT_OPTION_EMPTY);
    }

    @Override
    public void onNoInternet() {
        mAddQuestionView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onQuestionSaveSuccess() {
        mAddQuestionView.showMessage(Alerts.SUBMIT_QUESTION_SUCCESS);
        mAddQuestionView.goBack();
    }

    @Override
    public void onQuestionSaveFailed() {
        mAddQuestionView.showMessage(Alerts.API_FAIL);
    }
}