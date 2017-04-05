package in.sportscafe.nostragamus.module.user.login;

import android.content.Intent;
import android.os.Bundle;

public interface LogInPresenter {

	void onCreateLogIn(Bundle bundle);

	void onClickFacebook();

	void onClickGoogle();

	void onSuccessGoogleToken(String token, String personId, String personName, String persongender, String profileUrl, String personEmail, String personPhoto);

	void onClickSkip();

	void onActivityResult(int requestCode, int resultCode, Intent data);

    void onGetEmail(String email);
}