package com.jeeva.android.facebook.user;

import android.app.Activity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jeeva.android.facebook.FacebookConstants;

import java.util.List;

public class UserModelImpl implements FacebookConstants.ApiKeys {

    private UserModelListener mUserModelListener;

    public UserModelImpl(UserModelListener modelListener) {
        this.mUserModelListener = modelListener;
    }

    public void loginUser(Activity activity, CallbackManager callbackManager, final List<String> readPermissions) {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mUserModelListener.onFbLoginSuccess();

                // Todo comment out once you done the facebook review
                /*if (AccessToken.getCurrentAccessToken().getPermissions().contains(mFacebookPublishPermission)) {
                    mFbLoginListener.onFbLoginSuccess(loginResult);
                } else {
                    loginManager.logInWithPublishPermissions(activity, Arrays.asList(mFacebookPublishPermission));
                }*/
            }

            @Override
            public void onCancel() {
                mUserModelListener.onLoginCancelled();
            }

            @Override
            public void onError(FacebookException e) {
                mUserModelListener.onFbLoginFailed();
            }
        });
        loginManager.logInWithReadPermissions(activity, readPermissions);
    }

    public interface UserModelListener {

        void onFbLoginSuccess();

        void onLoginCancelled();

        void onFbLoginFailed();
    }
}