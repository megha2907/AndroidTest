package in.sportscafe.scgame.module.user.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.scgame.Constants;

public class LogInPresenterImpl implements LogInPresenter, LogInModelImpl.LogInModelListener {

    private LogInView mLogInView;

    private LogInModel mLogInModel;

    public LogInPresenterImpl(LogInView view) {
        this.mLogInView = view;
        this.mLogInModel = LogInModelImpl.newInstance(LogInPresenterImpl.this);
    }

    public static LogInPresenter newInstance(LogInView view) {
        return new LogInPresenterImpl(view);
    }

    @Override
    public void onCreateLogIn(Bundle bundle) {
        mLogInView.initViews();
    }

    @Override
    public void onClickFacebook() {
        mLogInModel.triggerFacebook();
    }

    @Override
    public void onClickGoogle() {
        mLogInView.signIn(null);
    }

    @Override
    public void onSuccessGoogleToken(String token) {
        mLogInModel.onLoggedInGoogle(token);
    }

    @Override
    public void onClickSkip() {
        mLogInView.navigateToHome();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLogInModel.onGetResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return mLogInView.getActivity();
    }

    @Override
    public void onLoginCompleted() {
        mLogInView.hideProgressDialog();

        if(mLogInModel.isPreferenceDone()) {
            mLogInView.navigateToHome();
        } else {
            mLogInView.navigateToSports();
        }
    }

    @Override
    public void onLoginCanceled() {
        mLogInView.hideProgressDialog();
        mLogInView.showMessage(Constants.Alerts.LOGIN_CANCELLED);
    }

    @Override
    public void onLoginFailed() {
        mLogInView.hideProgressDialog();
        mLogInView.showMessage(Constants.Alerts.LOGIN_FAILED);
    }

    @Override
    public void onNoInternet() {
        mLogInView.hideProgressDialog();
        mLogInView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onGettingProfile() {
        mLogInView.showProgressDialog();
    }
}