package in.sportscafe.scgame.module.user.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LogInPresenter {

	void onCreateLogIn(Bundle bundle);

	void onClickFacebook();

	void onClickGoogle();

	void onSuccessGoogleToken(String token);

	void onClickSkip();

	void onActivityResult(int requestCode, int resultCode, Intent data);
}