package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import com.jeeva.android.View;

/**
 * Created by deepanshi on 8/22/17.
 */

public interface EditUserProfileView extends View {
    void setProfileImage(String imageUrl);

    void setNickName(String nickname);

    void close();

    void setSuccessResult();

    void navigateToHome(boolean fromHome);

    void setNicknameEmpty();

    void setNicknameConflict();

    void setNicknameNotValid();

    void navigateToAddPhoto(int requestCode);
}
