package in.sportscafe.scgame.module.user.myprofile.edit;

import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditProfileModel {

    void updateProfile(String about);

    void updateProfilePhoto(MultipartBody.Part file, RequestBody filepath, RequestBody filename);

    UserInfo getUserInfo();
}