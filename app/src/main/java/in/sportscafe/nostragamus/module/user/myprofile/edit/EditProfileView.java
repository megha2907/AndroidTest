package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.os.Bundle;

import com.jeeva.android.View;

import static com.google.android.gms.analytics.internal.zzy.i;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfileView extends View {

    void setProfileImage(String imageUrl);

    void setNickName(String nickname);

    void close();

    void setSuccessResult();

    void navigateToHome(boolean fromHome);

    void setNicknameEmpty();

    void setNicknameConflict();

    void changeViewforProfile();

    void changeViewforLogin(String UserName);

    void setNicknameNotValid();

    void navigateToAddPhoto(int requestCode);

    void onIncorrectReferralCode();

    void onCorrectReferralCode();

    void navigateToSuccessfulReferral();

    void navigateToOTPVerification(boolean successfulReferral);
}