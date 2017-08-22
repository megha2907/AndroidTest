package in.sportscafe.nostragamus.module.navigation.edituserprofile;

import android.content.Intent;

import java.io.File;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by deepanshi on 8/22/17.
 */

public interface EditUserProfileModel {

    void updateProfile(String about);

    void onGetImage(Intent imageData);

    void updateProfilePhoto(File file, String filepath, String filename);

    UserInfo getUserInfo();
}
