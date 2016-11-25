package in.sportscafe.nostragamus.module.user.login;

import android.content.Intent;

public interface LogInModel {

    void triggerFacebook();

    void onLoggedInGoogle(String token);

    void onGetResult(int requestCode, int resultCode, Intent data);

    boolean isPreferenceDone();
}