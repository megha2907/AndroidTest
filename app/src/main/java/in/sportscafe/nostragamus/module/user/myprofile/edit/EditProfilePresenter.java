package in.sportscafe.nostragamus.module.user.myprofile.edit;

import android.os.Bundle;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfilePresenter {

    void onCreateEditProfile(Bundle bundle);

    void onClickDone(String about);

    void onProfilePhotoDone(File file, String filepath, String filename);

}