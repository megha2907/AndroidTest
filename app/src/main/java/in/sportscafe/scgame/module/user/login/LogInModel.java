package in.sportscafe.scgame.module.user.login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LogInModel {

    void triggerFacebook();

    void onLoggedInGoogle(String token);

    void onGetResult(int requestCode, int resultCode, Intent data);

    boolean isPreferenceDone();
}