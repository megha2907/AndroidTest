package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.content.Intent;

import java.io.File;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfileModel {

    void updateProfile(String about);

    void onGetImage(Intent imageData);

    void updateProfilePhoto(File file, String filepath, String filename);

    UserInfo getUserInfo();
}