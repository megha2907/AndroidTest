package in.sportscafe.scgame.module.user.myprofile.edit;

import com.jeeva.android.View;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfileView extends View {

    void setProfileImage(String imageUrl);

    void setName(String name);

//    void setUserName(String userName);

    void setAbout(String about);

    void close();

    void setSuccessResult();
}