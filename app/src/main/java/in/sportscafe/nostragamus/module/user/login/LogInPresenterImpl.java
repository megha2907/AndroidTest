package in.sportscafe.nostragamus.module.user.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;

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

        NostragamusAnalytics.getInstance().trackLogIn(AnalyticsActions.FACEBOOK);
    }

    @Override
    public void onClickGoogle() {
        mLogInView.signIn(null);

        NostragamusAnalytics.getInstance().trackLogIn(AnalyticsActions.GOOGLE_PLUS);
    }

    @Override
    public void onSuccessGoogleToken(String token, String personId, String personName, String persongender, String profileUrl, String personEmail, String personPhoto) {
        mLogInModel.onLoggedInGoogle(token,personId,personName,persongender,profileUrl,personEmail,personPhoto);
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
            mLogInView.navigateToEditProfile();
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