package in.sportscafe.nostragamus.module.user.login;

import android.content.Intent;

import com.jeeva.android.View;

public interface LogInView extends View {

    void initViews();

    void navigateToEditProfile();

    void signIn(Intent intent);

    void navigateToHome();

    void signOut();

    void showProgressDialog();

    void hideProgressDialog();

    void showEmailDialog();

    void dismissEmailDialog();
}