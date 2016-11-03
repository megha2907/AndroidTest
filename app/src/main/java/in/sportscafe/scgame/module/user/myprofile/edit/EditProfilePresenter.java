package in.sportscafe.scgame.module.user.myprofile.edit;

import android.os.Bundle;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfilePresenter {

    void onCreateEditProfile(Bundle bundle);

    void onClickDone(String about);

    void onProfilePhotoDone(MultipartBody.Part file, RequestBody filepath, RequestBody filename);

}