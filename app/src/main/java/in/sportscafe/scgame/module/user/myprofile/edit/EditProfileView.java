package in.sportscafe.scgame.module.user.myprofile.edit;

import android.content.Intent;

import com.jeeva.android.View;

import in.sportscafe.scgame.module.home.HomeActivity;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfileView extends View {

    void setProfileImage(String imageUrl);

    void setNickName(String nickname);

    void close();

    void setSuccessResult();

    void navigateToHome();

    void navigateToSportsSelection();

    void setNicknameEmpty();

    void setNicknameConflict();

    void changeViewforProfile();

    void changeViewforLogin(String UserName);

    void setNicknameNotValid();
}