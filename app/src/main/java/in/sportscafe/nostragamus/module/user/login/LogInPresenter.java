package in.sportscafe.nostragamus.module.user.login;

import android.content.Intent;
import android.os.Bundle;

public interface LogInPresenter {

	void onCreateLogIn(Bundle bundle);

	void onClickFacebook();

	void onClickGoogle();

	void onSuccessGoogleToken(String token);

	void onClickSkip();

	void onActivityResult(int requestCode, int resultCode, Intent data);
}