package in.sportscafe.nostragamus.module.getstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.feedback.GoogleFormActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;

/**
 * Created by Jeeva on 27/5/16.
 */
public class GetStartActivity extends Activity {

    private static final int GOOGLE_FORM_CODE = 90;

    private static final String GOOGLE_FORM_LINK = "https://docs.google.com/a/sportscafe.in/forms/d/e/1FAIpQLScFyPxEFIeCe1Yg3xiV_BhxKTDKDCm0PuzltgLPXz7iwWexLg/viewform?c=0&w=1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (NostragamusDataHandler.newInstance().isInitialFeedbackFormShown()) {*/
        handleGetStart();
        /*} else {
            navigateToForm();
        }*/
    }

    private void handleGetStart() {
        if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
            if (NostragamusDataHandler.getInstance().isFirstTimeUser()) {
                navigateToEditProfile();
            } else {
                navigateToHome();
            }
        } else {
            navigateToLogin();
        }
    }

    private void navigateToForm() {
        Intent intent = new Intent(this, GoogleFormActivity.class);
        intent.putExtra(BundleKeys.FEEDBACK_FORM_URL, GOOGLE_FORM_LINK);
        startActivityForResult(intent, GOOGLE_FORM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GOOGLE_FORM_CODE == requestCode) {
            handleGetStart();
        }
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("screen", BundleKeys.LOGIN_SCREEN);
        startActivity(intent);
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LogInActivity.class));
        finish();
    }
}